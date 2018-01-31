package stroom.query.testing.hibernate.app;

import com.codahale.metrics.health.HealthCheck;
import event.logging.EventLoggingService;
import io.dropwizard.Application;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.flyway.FlywayBundle;
import io.dropwizard.flyway.FlywayFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import stroom.query.audit.authorisation.AuthorisationService;
import stroom.query.audit.rest.AuditedDocRefResourceImpl;
import stroom.query.audit.rest.AuditedQueryResourceImpl;
import stroom.query.audit.service.DocRefService;
import stroom.query.audit.service.QueryService;
import stroom.query.hibernate.AuditedCriteriaQueryBundle;

import javax.inject.Inject;

public class HibernateApp extends Application<HibernateConfig> {
    // Wrap the flyway bundle so that we can call migrate in the bundles 'run'.
    // This allows the flyway migration to happen before the hibernate validation
    private final ConfiguredBundle<HibernateConfig> flywayBundle = new ConfiguredBundle<HibernateConfig>() {

        private final FlywayBundle<HibernateConfig> wrappedBundle = new FlywayBundle<HibernateConfig>() {
            public DataSourceFactory getDataSourceFactory(HibernateConfig config) {
                return config.getDataSourceFactory();
            }

            public FlywayFactory getFlywayFactory(final HibernateConfig config) {
                return config.getFlywayFactory();
            }
        };

        @Override
        public void run(final HibernateConfig configuration, final Environment environment) throws Exception {
            wrappedBundle.run(environment);

            final ManagedDataSource dataSource = configuration.getDataSourceFactory()
                    .build(environment.metrics(), "flywayDataSource");
            configuration.getFlywayFactory()
                    .build(dataSource)
                    .migrate();
        }

        @Override
        public void initialize(Bootstrap<?> bootstrap) {
            wrappedBundle.initialize(bootstrap);
        }

    };

    public static final class AuditedDocRefResource extends AuditedDocRefResourceImpl<TestDocRefHibernateEntity> {

        @Inject
        public AuditedDocRefResource(final DocRefService<TestDocRefHibernateEntity> service,
                                            final EventLoggingService eventLoggingService,
                                            final AuthorisationService authorisationService) {
            super(service, eventLoggingService, authorisationService);
        }
    }

    public static final class AuditedQueryResource extends AuditedQueryResourceImpl<TestDocRefHibernateEntity> {

        @Inject
        public AuditedQueryResource(final EventLoggingService eventLoggingService,
                                           final QueryService service,
                                           final AuthorisationService authorisationService,
                                           final DocRefService<TestDocRefHibernateEntity> docRefService) {
            super(eventLoggingService, service, authorisationService, docRefService);
        }
    }

    private final AuditedCriteriaQueryBundle<HibernateConfig,
            TestQueryableEntity,
            TestDocRefHibernateEntity,
            AuditedQueryResource,
            TestDocRefServiceCriteriaImpl,
            AuditedDocRefResource> auditedQueryBundle =
            new AuditedCriteriaQueryBundle<>(
                    TestQueryableEntity.class,
                    new HibernateBundle<HibernateConfig>(TestDocRefHibernateEntity.class, TestQueryableEntity.class) {
                        @Override
                        public DataSourceFactory getDataSourceFactory(HibernateConfig configuration) {
                            return configuration.getDataSourceFactory();
                        }
                    },
                    TestDocRefHibernateEntity.class,
                    AuditedQueryResource.class,
                    TestDocRefServiceCriteriaImpl.class,
                    AuditedDocRefResource.class);

    @Override
    public void run(final HibernateConfig configuration,
                    final Environment environment) throws Exception {
        environment.healthChecks().register("Something", new HealthCheck() {
            @Override
            protected Result check() {
                return Result.healthy("Keeps Dropwizard Happy");
            }
        });
    }

    @Override
    public void initialize(final Bootstrap<HibernateConfig> bootstrap) {
        super.initialize(bootstrap);

        // This allows us to use templating in the YAML configuration.
        bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(
                bootstrap.getConfigurationSourceProvider(),
                new EnvironmentVariableSubstitutor(false)));

        bootstrap.addBundle(this.flywayBundle);
        bootstrap.addBundle(this.auditedQueryBundle);
    }
}
