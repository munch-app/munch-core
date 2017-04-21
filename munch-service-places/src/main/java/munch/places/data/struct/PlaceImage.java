package munch.places.data.struct;

/**
 * Created by: Fuxing
 * Date: 21/4/2017
 * Time: 1:47 AM
 * Project: munch-core
 */
public final class PlaceImage {
    // TODO place image
    // public static final String FROM_RESTAURANT = "restaurant"; // Removed Due to confusion
    public static final String FROM_GALLERY = "gallery";
    public static final String FROM_ARTICLE = "article";
    public static final String FROM_REVIEW = "review";

    private String placeId;
    private String linkedFrom;
    private String linkedId;

    private String imageKey;

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getLinkedFrom() {
        return linkedFrom;
    }

    public void setLinkedFrom(String linkedFrom) {
        this.linkedFrom = linkedFrom;
    }

    public String getLinkedId() {
        return linkedId;
    }

    public void setLinkedId(String linkedId) {
        this.linkedId = linkedId;
    }

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }
}
