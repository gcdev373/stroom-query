package stroom.query.api.v2;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class DocRefBuilderTest {
    @Test
    public void doesBuild() {
        final String name = "someName";
        final String type = "someType";
        final String uuid = UUID.randomUUID().toString();

        final DocRef docRef = new DocRef.Builder()
                .name(name)
                .type(type)
                .uuid(uuid)
                .build();

        assertThat(docRef.getName()).isEqualTo(name);
        assertThat(docRef.getType()).isEqualTo(type);
        assertThat(docRef.getUuid()).isEqualTo(uuid);
    }
}
