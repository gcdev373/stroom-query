package stroom.query.jooq;

import com.bendb.dropwizard.jooq.JooqBundle;
import com.bendb.dropwizard.jooq.JooqFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.flyway.FlywayBundle;
import io.dropwizard.flyway.FlywayFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stroom.query.audit.AuditedQueryBundle;
import stroom.query.authorisation.HasAuthorisationConfig;
import stroom.query.audit.model.IsDataSourceField;
import stroom.query.security.HasTokenConfig;
import stroom.query.audit.service.DocRefService;
import stroom.query.audit.service.QueryService;

import java.util.function.Function;

/**
 * This Dropwizard bundle can be used to build the entire Query Resource implementation stack when the data source is
 * an SQL database, storing one annotated data type. The annotated data type will need to also annotate it's fields with
 * {@link IsDataSourceField}
 *
 * This more general implementation allows any QueryService implementation to be given.
 *
 * @param <CONFIG> The configuration class
 * @param <QUERY_SERVICE> The query service implementation.
 * @param <DOC_REF_POJO> POJO class for the Document
 * @param <DOC_REF_SERVICE> Implementation class for the DocRef Service
 */
public class AuditedJooqDocRefBundle<CONFIG extends Configuration & HasTokenConfig & HasAuthorisationConfig & HasDataSourceFactory & HasFlywayFactory & HasJooqFactory,
        DOC_REF_SERVICE extends DocRefService<DOC_REF_POJO>,
        DOC_REF_POJO extends DocRefJooqEntity,
        QUERY_SERVICE extends QueryService> implements ConfiguredBundle<CONFIG> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuditedJooqQueryBundle.class);

    private final FlywayBundle flywayBundle = new FlywayBundle<CONFIG>() {
        public DataSourceFactory getDataSourceFactory(CONFIG config) {
            return config.getDataSourceFactory();
        }

        public FlywayFactory getFlywayFactory(CONFIG config) {
            return config.getFlywayFactory();
        }
    };

    private final JooqBundle<CONFIG> jooqBundle;

    private final AuditedQueryBundle<CONFIG,
            DOC_REF_SERVICE,
            DOC_REF_POJO,
            QUERY_SERVICE> auditedQueryBundle;

    public AuditedJooqDocRefBundle(final Function<CONFIG, Injector> injectorSupplier,
                                   final Class<DOC_REF_SERVICE> docRefServiceClass,
                                   final Class<DOC_REF_POJO> docRefEntityClass,
                                   final Class<QUERY_SERVICE> queryServiceClass) {
        auditedQueryBundle = new AuditedQueryBundle<>(injectorSupplier, docRefServiceClass, docRefEntityClass, queryServiceClass);

        this.jooqBundle = new JooqBundle<CONFIG>() {
            public DataSourceFactory getDataSourceFactory(CONFIG configuration) {
                return configuration.getDataSourceFactory();
            }

            public JooqFactory getJooqFactory(CONFIG configuration) {
                return configuration.getJooqFactory();
            }
        };
    }

    public Module getGuiceModule(CONFIG configuration) {
        return Modules.combine(new AbstractModule() {
            @Override
            protected void configure() {
                bind(DSLContext.class).toInstance(DSL.using(jooqBundle.getConfiguration()));
            }
        }, auditedQueryBundle.getGuiceModule(configuration));
    }

    @Override
    public void run(final CONFIG configuration,
                    final Environment environment) {
        // We need the database before we need most other things
        migrate(configuration, environment);
    }

    private void migrate(CONFIG config, Environment environment) {
        ManagedDataSource dataSource = config.getDataSourceFactory().build(environment.metrics(), "flywayDataSource");
        Flyway flyway = config.getFlywayFactory().build(dataSource);
        // We want to be resilient against the database not being available, so we'll keep trying to migrate if there's
        // an exception. This approach blocks the startup of the service until the database is available. The downside
        // of this is that the admin pages won't be available - any future dashboarding that wants to emit information
        // about the missing database won't be able to do so. The upside of this approach is that it's very simple
        // to implement from where we are now, i.e. we don't need to add service-wide code to handle a missing database
        // e.g. in JwkDao.init().
        boolean migrationComplete = false;
        int databaseRetryDelayMs = 5000;
        while(!migrationComplete){
            try {
                flyway.migrate();
                migrationComplete = true;
            } catch(FlywayException flywayException){
                LOGGER.error("Unable to migrate database! Will retry in {}ms.", databaseRetryDelayMs);
                try {
                    Thread.sleep(databaseRetryDelayMs);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initialize(final Bootstrap<?> bootstrap) {
        final Bootstrap<CONFIG> castBootstrap = (Bootstrap<CONFIG>) bootstrap; // this initialize function should have used the templated config type
        castBootstrap.addBundle(jooqBundle);
        castBootstrap.addBundle(flywayBundle);
        castBootstrap.addBundle(auditedQueryBundle);
    }
}
