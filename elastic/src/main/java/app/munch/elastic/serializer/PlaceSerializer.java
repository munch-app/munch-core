package app.munch.elastic.serializer;

import app.munch.model.ElasticDocument;
import app.munch.model.ElasticDocumentType;
import app.munch.model.Place;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 9/9/19
 * Time: 10:24 pm
 */
@Singleton
public final class PlaceSerializer implements Serializer<Place>, TimingsSerializer {

    @Override
    public ElasticDocument serialize(Place place) {
        ElasticDocument document = new ElasticDocument(ElasticDocumentType.PLACE, builder -> {
            builder.type(ElasticDocumentType.PLACE);
            builder.id(place.getId());
        });

        document.setSuggest(builder -> {
            builder.type(ElasticDocumentType.PLACE);
            builder.latLng(place.getLocation().getLatLng());
            builder.input(place.getName());
        });

        document.setId(place.getId());
        document.setSlug(place.getSlug());

        document.setName(place.getName());
        document.setPhone(place.getPhone());
        document.setWebsite(place.getWebsite());
        document.setDescription(place.getDescription());
        document.setImage(place.getImage());
        document.setImportant(place.getImportant());

        document.setSynonyms(place.getSynonyms());
        document.setTags(place.getTags());
        document.setHours(place.getHours());
        document.setStatus(place.getStatus());

        document.setLocation(serializeLocation(place.getLocation()));
        document.setPrice(serializePrice(place.getPrice()));

        document.setUpdatedAt(place.getUpdatedAt());
        document.setCreatedAt(place.getCreatedAt());

        document.setTimings(serializeTimings(place.getHours()));
        return document;
    }

    @NotNull
    private ElasticDocument.Location serializeLocation(Place.Location data) {
        Objects.requireNonNull(data);

        ElasticDocument.Location location = new ElasticDocument.Location();
        location.setLatLng(data.getLatLng());
        location.setAddress(data.getAddress());
        location.setPostcode(data.getPostcode());
        location.setUnitNumber(data.getUnitNumber());
        return location;
    }

    @Nullable
    private ElasticDocument.Price serializePrice(Place.Price data) {
        if (data == null) return null;

        ElasticDocument.Price price = new ElasticDocument.Price();
        price.setPerPax(data.getPerPax());
        return price;
    }
}
