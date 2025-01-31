package stroom.query.testing.jooq.app;

import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import stroom.query.jooq.AuditedJooqQueryBundle;

public class JooqApp extends Application<JooqConfig> {
    private Injector injector;

    private AuditedJooqQueryBundle<JooqConfig,
            TestDocRefServiceJooqImpl,
            TestDocRefJooqEntity,
            TestQueryableJooqEntity> auditedQueryBundle;

    public static void main(final String[] args) throws Exception {
        new JooqApp().run(args);
    }

    @Override
    public void run(final JooqConfig configuration,
                    final Environment environment) {

        environment.healthChecks().register("Something", new HealthCheck() {
            @Override
            protected Result check() {
                return Result.healthy("Keeps Dropwizard Happy");
            }
        });

        environment.jersey().register(injector.getInstance(CreateTestDataJooqImpl.class));
    }

    @Override
    public void initialize(final Bootstrap<JooqConfig> bootstrap) {
        super.initialize(bootstrap);

        auditedQueryBundle =
                new AuditedJooqQueryBundle<>(
                        (c) -> {
                            injector = Guice.createInjector(auditedQueryBundle.getGuiceModule(c));
                            return injector;
                        },
                        TestDocRefServiceJooqImpl.class,
                        TestDocRefJooqEntity.class,
                        TestQueryableJooqEntity.class);

        // This allows us to use templating in the YAML configuration.
        bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(
                bootstrap.getConfigurationSourceProvider(),
                new EnvironmentVariableSubstitutor(false)));

        bootstrap.addBundle(this.auditedQueryBundle);
    }
}
