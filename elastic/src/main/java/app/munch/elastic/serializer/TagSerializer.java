package app.munch.elastic.serializer;

import app.munch.model.ElasticDocument;
import app.munch.model.ElasticDocumentType;
import app.munch.model.Tag;

import javax.inject.Singleton;

/**
 * @author Fuxing Loh
 * @since 2019-11-25 at 15:11
 */
@Singleton
public final class TagSerializer implements Serializer<Tag> {
    @Override
    public ElasticDocument serialize(Tag tag) {
        ElasticDocument document = new ElasticDocument(ElasticDocumentType.TAG, builder -> {
            builder.type(ElasticDocumentType.TAG);
            builder.id(tag.getId());
        });

        document.setSuggest(builder -> {
            builder.type(ElasticDocumentType.TAG);
            builder.input(tag.getName());
        });

        document.setId(tag.getId());
        document.setType(tag.getType().toString());
        document.setName(tag.getName());

        document.setUpdatedAt(tag.getUpdatedAt());
        document.setCreatedAt(tag.getCreatedAt());
        return document;
    }
}
