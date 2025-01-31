package stroom.query.testing.jooq.app;

import com.bendb.dropwizard.jooq.JooqFactory;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayFactory;
import stroom.query.authorisation.AuthorisationServiceConfig;
import stroom.query.authorisation.HasAuthorisationConfig;
import stroom.query.jooq.HasDataSourceFactory;
import stroom.query.jooq.HasFlywayFactory;
import stroom.query.jooq.HasJooqFactory;
import stroom.query.security.HasTokenConfig;
import stroom.query.security.TokenConfig;

public class JooqConfig extends Configuration implements HasAuthorisationConfig, HasTokenConfig, HasDataSourceFactory, HasFlywayFactory, HasJooqFactory {
    @JsonProperty("database")
    private DataSourceFactory dataSourceFactory = new DataSourceFactory();

    @JsonProperty("jooq")
    private JooqFactory jooqFactory = new JooqFactory();

    @JsonProperty("flyway")
    private FlywayFactory flywayFactory = new FlywayFactory();

    @JsonProperty("token")
    private TokenConfig tokenConfig;

    @JsonProperty("authorisationService")
    private AuthorisationServiceConfig authorisationServiceConfig;

    public final DataSourceFactory getDataSourceFactory() {
        return this.dataSourceFactory;
    }

    public final FlywayFactory getFlywayFactory() {
        return this.flywayFactory;
    }

    public final JooqFactory getJooqFactory() {
        return jooqFactory;
    }

    @Override
    public TokenConfig getTokenConfig() {
        return tokenConfig;
    }

    @Override
    public AuthorisationServiceConfig getAuthorisationServiceConfig() {
        return authorisationServiceConfig;
    }
}
