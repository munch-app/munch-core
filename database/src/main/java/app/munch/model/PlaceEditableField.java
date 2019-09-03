package app.munch.model;

import java.util.Set;
import java.util.function.BiConsumer;

/**
 * This enum dictates whether a each field is editable.
 * Some service in munch expose PlaceRevision for editing, but not all fields are editable by the client.
 * This enum will ensure that field that are not editable will not be edited.
 * <p>
 * Created by: Fuxing
 * Date: 3/9/19
 * Time: 3:57 pm
 */
public enum PlaceEditableField {
    // Single Field
    NAME((revision, model) -> revision.setName(model.getName())),
    PHONE((revision, model) -> revision.setPhone(model.getPhone())),
    WEBSITE((revision, model) -> revision.setWebsite(model.getWebsite())),
    DESCRIPTION((revision, model) -> revision.setDescription(model.getDescription())),

    // Price is a nullable object, if null is given, it will be set to null
    PRICE((revision, model) -> {
        revision.setPrice(model.getPrice());
    }),

    // Location is a required object, if {@code null}, location object will not be edited
    LOCATION_UNIT_NUMBER((revision, model) -> {
        PlaceModel.Location location = model.getLocation();
        if (location == null) return;
        revision.getLocation().setUnitNumber(location.getUnitNumber());
    }),
    LOCATION_POSTCODE((revision, model) -> {
        PlaceModel.Location location = model.getLocation();
        if (location == null) return;
        revision.getLocation().setPostcode(location.getPostcode());
    }),
    LOCATION_ADDRESS((revision, model) -> {
        PlaceModel.Location location = model.getLocation();
        if (location == null) return;
        revision.getLocation().setAddress(location.getAddress());
    }),
    LOCATION_LAT_LNG((revision, model) -> {
        PlaceModel.Location location = model.getLocation();
        if (location == null) return;
        revision.getLocation().setLatLng(location.getLatLng());
    }),

    // Status is a required object, if {@code null}, location object will not be edited
    // When status is edited, the entire object is replaced
    STATUS((revision, model) -> {
        if (model.getStatus() == null) return;
        revision.setStatus(model.getStatus());
    }),

    // List Objects, Null Check Enabled
    SYNONYMS((revision, model) -> {
        if (model.getSynonyms() == null) return;
        revision.setSynonyms(model.getSynonyms());
    }),
    TAGS((revision, model) -> {
        if (model.getTags() == null) return;
        revision.setTags(model.getTags());
    }),
    HOURS((revision, model) -> {
        if (model.getHours() == null) return;
        revision.setHours(model.getHours());
    }),
    ;

    /**
     * All of the editable field.
     */
    public static final Set<PlaceEditableField> ALL = Set.of(
            NAME, PHONE, WEBSITE, DESCRIPTION,
            PRICE, STATUS,
            LOCATION_POSTCODE, LOCATION_ADDRESS, LOCATION_LAT_LNG, LOCATION_UNIT_NUMBER,
            SYNONYMS, TAGS, HOURS
    );

    private final BiConsumer<PlaceRevision, PlaceModel> mapper;

    PlaceEditableField(BiConsumer<PlaceRevision, PlaceModel> mapper) {
        this.mapper = mapper;
    }

    public void edit(PlaceRevision revision, PlaceModel value) {
        mapper.accept(revision, value);
    }
}
