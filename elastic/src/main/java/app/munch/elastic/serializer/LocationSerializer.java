package app.munch.elastic.serializer;

import app.munch.model.ElasticDocument;
import app.munch.model.ElasticDocumentType;
import app.munch.model.Location;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;

/**
 * @author Fuxing Loh
 * @since 2019-11-25 at 14:13
 */
@Singleton
public final class LocationSerializer implements Serializer<Location> {

    @Override
    public ElasticDocument serialize(Location location) {
        ElasticDocument document = new ElasticDocument(ElasticDocumentType.LOCATION, builder -> {
            builder.type(ElasticDocumentType.LOCATION);
            builder.id(location.getId());
        });

        document.setSuggest(builder -> {
            builder.type(ElasticDocumentType.LOCATION);
            builder.latLng(location.getLatLng());
            builder.input(location.getName());
        });

        document.setId(location.getId());
        document.setSlug(location.getSlug());
        document.setImage(location.getImage());

        document.setName(location.getName());
        document.setName(location.getType().toString());
        document.setDescription(location.getDescription());
        document.setLocation(serializeLocation(location));

        document.setSynonyms(location.getSynonyms());
        document.setUpdatedAt(location.getUpdatedAt());
        document.setCreatedAt(location.getCreatedAt());
        return document;
    }

    @NotNull
    private ElasticDocument.Location serializeLocation(Location data) {
        ElasticDocument.Location location = new ElasticDocument.Location();
        location.setLatLng(data.getLatLng());
        location.setAddress(data.getAddress());
        location.setPostcode(data.getPostcode());
        location.setGeometry(data.getGeometry());
        return location;
    }
}
