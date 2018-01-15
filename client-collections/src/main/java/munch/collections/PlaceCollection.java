package munch.collections;

import java.util.*;

/**
 * Created by: Fuxing
 * Date: 9/1/18
 * Time: 4:20 PM
 * Project: munch-core
 */
public final class PlaceCollection {
    private String userId;
    private String collectionId;

    private String sortKey;

    private String name;
    private String description;

    private Map<String, String> thumbnail;
    private List<AddedPlace> addedPlaces = new ArrayList<>();

    private Date updatedDate;
    private Date createdDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Map<String, String> thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<AddedPlace> getAddedPlaces() {
        return addedPlaces;
    }

    public void setAddedPlaces(List<AddedPlace> addedPlaces) {
        this.addedPlaces = addedPlaces;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public static class AddedPlace {
        private String placeId;
        private Date createdDate;

        public String getPlaceId() {
            return placeId;
        }

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        public Date getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(Date createdDate) {
            this.createdDate = createdDate;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AddedPlace that = (AddedPlace) o;
            return Objects.equals(placeId, that.placeId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(placeId);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceCollection that = (PlaceCollection) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(collectionId, that.collectionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, collectionId);
    }
}
