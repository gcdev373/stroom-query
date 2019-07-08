package stroom.query.testing.hibernate;


import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.extension.ExtendWith;
import stroom.datasource.api.v2.DataSource;
import stroom.datasource.api.v2.AbstractField;
import stroom.docref.DocRef;
import stroom.query.api.v2.*;
import stroom.query.audit.model.DocRefEntity;
import stroom.query.testing.DatabaseContainerExtension;
import stroom.query.testing.DatabaseContainerExtensionSupport;
import stroom.query.testing.DropwizardAppExtensionWithClients;
import stroom.query.testing.QueryResourceNoAuthIT;
import stroom.query.testing.hibernate.app.HibernateApp;
import stroom.query.testing.hibernate.app.HibernateConfig;
import stroom.query.testing.hibernate.app.TestDocRefHibernateEntity;
import stroom.query.testing.hibernate.app.TestQueryableHibernateEntity;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(DatabaseContainerExtensionSupport.class)
@ExtendWith(DropwizardExtensionsSupport.class)
class TestHibernateQueryResourceNoAuthIT extends QueryResourceNoAuthIT<TestDocRefHibernateEntity, HibernateConfig> {

    private static final DatabaseContainerExtension dbRule = new DatabaseContainerExtension();
    private static final DropwizardAppExtensionWithClients<HibernateConfig> appRule =
            new DropwizardAppExtensionWithClients<>(HibernateApp.class, resourceFilePath("hibernate_noauth/config.yml"));

    TestHibernateQueryResourceNoAuthIT() {
        super(TestDocRefHibernateEntity.TYPE,
                appRule);
    }

    @Override
    protected SearchRequest getValidSearchRequest(final DocRef docRef,
                                                  final ExpressionOperator expressionOperator,
                                                  final OffsetRange offsetRange) {
        final String queryKey = UUID.randomUUID().toString();
        return new SearchRequest.Builder()
                .query(new Query.Builder()
                        .dataSource(docRef)
                        .expression(expressionOperator)
                        .build())
                .key(queryKey)
                .dateTimeLocale("en-gb")
                .incremental(true)
                .addResultRequests(new ResultRequest.Builder()
                        .fetch(ResultRequest.Fetch.ALL)
                        .resultStyle(ResultRequest.ResultStyle.FLAT)
                        .componentId("componentId")
                        .requestedRange(offsetRange)
                        .addMappings(new TableSettings.Builder()
                                .queryId(queryKey)
                                .extractValues(false)
                                .showDetail(false)
                                .addFields(new Field.Builder()
                                        .name(TestQueryableHibernateEntity.FLAVOUR)
                                        .expression("${" + TestQueryableHibernateEntity.FLAVOUR + "}")
                                        .build())
                                .addMaxResults(10)
                                .build())
                        .build())
                .build();
    }

    @Override
    protected void assertValidDataSource(final DataSource dataSource) {
        final Set<String> resultFieldNames = dataSource.getFields().stream()
                .map(AbstractField::getName)
                .collect(Collectors.toSet());

        assertThat(resultFieldNames.contains(DocRefEntity.CREATE_TIME)).isTrue();
        assertThat(resultFieldNames.contains(DocRefEntity.CREATE_USER)).isTrue();
        assertThat(resultFieldNames.contains(DocRefEntity.UPDATE_TIME)).isTrue();
        assertThat(resultFieldNames.contains(DocRefEntity.UPDATE_USER)).isTrue();
        assertThat(resultFieldNames.contains(TestQueryableHibernateEntity.ID)).isTrue();
        assertThat(resultFieldNames.contains(TestQueryableHibernateEntity.FLAVOUR)).isTrue();
    }

    @Override
    protected TestDocRefHibernateEntity getValidEntity(final DocRef docRef) {
        return new TestDocRefHibernateEntity.Builder()
                .docRef(docRef)
                .clanName("ClanName")
                .build();
    }
}
