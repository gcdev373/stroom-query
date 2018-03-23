package stroom.query.audit;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import event.logging.EventLoggingService;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import stroom.query.audit.authorisation.AuthorisationService;
import stroom.query.audit.authorisation.AuthorisationServiceConfig;
import stroom.query.audit.authorisation.AuthorisationServiceImpl;
import stroom.query.audit.authorisation.HasAuthorisationConfig;
import stroom.query.audit.authorisation.NoAuthAuthorisationServiceImpl;
import stroom.query.audit.model.DocRefEntity;
import stroom.query.audit.rest.AuditedDocRefResourceImpl;
import stroom.query.audit.rest.AuditedQueryResourceImpl;
import stroom.query.audit.security.HasTokenConfig;
import stroom.query.audit.security.NoAuthValueFactoryProvider;
import stroom.query.audit.security.RobustJwtAuthFilter;
import stroom.query.audit.security.ServiceUser;
import stroom.query.audit.security.TokenConfig;
import stroom.query.audit.service.DocRefService;
import stroom.query.audit.service.QueryService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * This Dropwizard bundle can be used to register an implementation of Query Resource implementation
 * This bundle will wrap that implementation in an audited layer, any requests made to your Query Resource
 * will be passed to the {@link QueryEventLoggingService}
 *
 * It will also register an audited version of the external DocRef resource. External DataSources will need to provide
 * implementations of DocRef resource to allow stroom to manage the documents that live outside of it.
 *
 * @param <CONFIG> The configuration class
 * @param <DOC_REF_POJO> POJO class for the Document
 * @param <QUERY_SERVICE> Implementation class for the Query Service
 * @param <DOC_REF_SERVICE> Implementation class for the DocRef Service
 */
public class AuditedQueryBundle<CONFIG extends Configuration & HasTokenConfig & HasAuthorisationConfig,
        DOC_REF_SERVICE extends DocRefService<DOC_REF_POJO>,
        DOC_REF_POJO extends DocRefEntity,
        QUERY_SERVICE extends QueryService> implements ConfiguredBundle<CONFIG> {

    private Injector injector;
    protected final Class<DOC_REF_SERVICE> docRefServiceClass;
    protected final Class<DOC_REF_POJO> docRefEntityClass;
    private final Class<QUERY_SERVICE> queryServiceClass;

    public AuditedQueryBundle(final Class<DOC_REF_SERVICE> docRefServiceClass,
                              final Class<DOC_REF_POJO> docRefEntityClass,
                              final Class<QUERY_SERVICE> queryServiceClass) {
        this.docRefServiceClass = docRefServiceClass;
        this.docRefEntityClass = docRefEntityClass;
        this.queryServiceClass = queryServiceClass;
    }

    /**
     * This function will be overridden by child classes that have further specific modules to register.
     * @param configuration The dropwizard application configuration
     * @param moduleConsumer A consumer for Guice modules
     */
    protected void iterateGuiceModules(final CONFIG configuration,
                                       final Consumer<Module> moduleConsumer) {
        moduleConsumer.accept(new AbstractModule() {
            @Override
            @SuppressWarnings("unchecked")
            protected void configure() {
                bind(EventLoggingService.class).to(QueryEventLoggingService.class);
                bind(QueryService.class).to(queryServiceClass);
                bind(DocRefEntity.ClassProvider.class).toInstance(new DocRefEntity.ClassProvider<>(docRefEntityClass));
                bind(DocRefService.class).to(docRefServiceClass);

                if (configuration.getTokenConfig().getSkipAuth()) {
                    bind(AuthorisationService.class).to(NoAuthAuthorisationServiceImpl.class);
                } else {
                    bind(AuthorisationService.class).to(AuthorisationServiceImpl.class);
                    bind(AuthorisationServiceConfig.class).toInstance(configuration.getAuthorisationServiceConfig());
                    bind(TokenConfig.class).toInstance(configuration.getTokenConfig());
                }
            }
        });
    }

    public Injector getInjector() {
        return injector;
    }

    @Override
    public void initialize(final Bootstrap<?> bootstrap) {

    }

    @Override
    public void run(final CONFIG configuration,
                    final Environment environment) {
        final List<Module> modules = new ArrayList<>();
        iterateGuiceModules(configuration, modules::add);

        injector = Guice.createInjector(modules);

        environment.jersey().register(injector.getInstance(AuditedQueryResourceImpl.class));
        environment.jersey().register(injector.getInstance(AuditedDocRefResourceImpl.class));

        // Configure auth
        if (configuration.getTokenConfig().getSkipAuth()) {
            environment.jersey().register(new NoAuthValueFactoryProvider.Binder());
        } else {
            environment.jersey().register(
                    new AuthDynamicFeature(
                            new RobustJwtAuthFilter(configuration.getTokenConfig())
                    ));
            environment.jersey().register(new AuthValueFactoryProvider.Binder<>(ServiceUser.class));
            environment.jersey().register(RolesAllowedDynamicFeature.class);
        }
    }
}
