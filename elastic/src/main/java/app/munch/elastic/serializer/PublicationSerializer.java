package app.munch.elastic.serializer;

import app.munch.model.ElasticDocument;
import app.munch.model.ElasticDocumentType;
import app.munch.model.Publication;

import javax.inject.Singleton;

/**
 * @author Fuxing Loh
 * @since 2019-11-25 at 15:11
 */
@Singleton
public final class PublicationSerializer implements Serializer<Publication> {
    @Override
    public ElasticDocument serialize(Publication data) {
        ElasticDocument document = new ElasticDocument(ElasticDocumentType.PUBLICATION, builder -> {
            builder.type(ElasticDocumentType.PUBLICATION);
            builder.id(data.getId());
        });

        document.setSuggest(builder -> {
            builder.type(ElasticDocumentType.PUBLICATION);
            builder.input(data.getName());
        });

        document.setId(data.getId());
        document.setName(data.getName());
        document.setDescription(data.getDescription());

        document.setImage(data.getImage());
        document.setTags(data.getTags());

        document.setUpdatedAt(data.getUpdatedAt());
        document.setCreatedAt(data.getCreatedAt());
        return document;
    }
}
