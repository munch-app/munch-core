package munch.collections;

/**
 * Created by: Fuxing
 * Date: 9/1/18
 * Time: 4:29 PM
 * Project: munch-core
 */
public final class AddedPlace {
    private String userId;
    private String placeId;

    private String sortKey;

    private String createdDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
