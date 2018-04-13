package stroom.query.testing.generic;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.ClassRule;
import stroom.datasource.api.v2.DataSource;
import stroom.datasource.api.v2.DataSourceField;
import stroom.query.api.v2.DocRef;
import stroom.query.api.v2.ExpressionOperator;
import stroom.query.api.v2.Field;
import stroom.query.api.v2.OffsetRange;
import stroom.query.api.v2.Query;
import stroom.query.api.v2.ResultRequest;
import stroom.query.api.v2.SearchRequest;
import stroom.query.api.v2.TableSettings;
import stroom.query.testing.DropwizardAppWithClientsRule;
import stroom.query.testing.QueryRemoteServiceIT;
import stroom.query.testing.StroomAuthenticationRule;
import stroom.query.testing.generic.app.App;
import stroom.query.testing.generic.app.Config;
import stroom.query.testing.generic.app.TestDocRefEntity;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;
import static org.junit.Assert.assertTrue;

public class TestQueryRemoteServiceIT extends QueryRemoteServiceIT<TestDocRefEntity, Config> {

    @ClassRule
    public static final DropwizardAppWithClientsRule<Config> appRule =
            new DropwizardAppWithClientsRule<>(App.class, resourceFilePath("generic/config.yml"));

    @ClassRule
    public static StroomAuthenticationRule authRule =
            new StroomAuthenticationRule(WireMockConfiguration.options().port(10080));

    public TestQueryRemoteServiceIT() {
        super(TestDocRefEntity.TYPE,
                TestDocRefEntity.class,
                appRule,
                authRule);
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
                                        .name(TestDocRefEntity.INDEX_NAME)
                                        .expression("${" + TestDocRefEntity.INDEX_NAME + "}")
                                        .build())
                                .addMaxResults(10)
                                .build())
                        .build())
                .build();
    }

    @Override
    protected void assertValidDataSource(final DataSource dataSource) {
        final Set<String> resultFieldNames = dataSource.getFields().stream()
                .map(DataSourceField::getName)
                .collect(Collectors.toSet());

        assertTrue(resultFieldNames.contains(TestDocRefEntity.INDEX_NAME));
    }

    @Override
    protected TestDocRefEntity getValidEntity(final DocRef docRef) {
        return new TestDocRefEntity.Builder()
                .docRef(docRef)
                .indexName("TestIndex")
                .build();
    }
}