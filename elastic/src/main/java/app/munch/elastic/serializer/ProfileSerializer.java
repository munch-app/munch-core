package app.munch.elastic.serializer;

import app.munch.model.ElasticDocument;
import app.munch.model.ElasticDocumentType;
import app.munch.model.Profile;

import javax.inject.Singleton;

/**
 * @author Fuxing Loh
 * @since 2019-11-25 at 15:11
 */
@Singleton
public final class ProfileSerializer implements Serializer<Profile> {
    @Override
    public ElasticDocument serialize(Profile data) {
        ElasticDocument document = new ElasticDocument(ElasticDocumentType.PROFILE, builder -> {
            builder.type(ElasticDocumentType.PROFILE);
            builder.uid(data.getUid());
        });

        document.setSuggest(builder -> {
            builder.type(ElasticDocumentType.PROFILE);
            builder.input(data.getName());
        });

        // 'username' is mapped into 'id'
        document.setId(data.getUsername());

        // 'bio' is mapped into 'description'
        document.setDescription(data.getBio());

        document.setUid(data.getUid());
        document.setName(data.getName());

        document.setImage(data.getImage());
        document.setUpdatedAt(data.getUpdatedAt());
        document.setCreatedAt(data.getCreatedAt());
        return document;
    }
}
