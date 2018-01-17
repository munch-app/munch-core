package munch.collections;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 9/1/18
 * Time: 4:20 PM
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class PlaceCollection {

    private String userId;
    private String collectionId;

    private String sortKey;

    private String name;
    private String description;
    private long count;

    private Map<String, String> thumbnail;
    private Date updatedDate;
    private Date createdDate;

    @NotNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @NotNull
    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    @NotNull
    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    @NotNull
    @Size(min = 3, max = 100, message = "Name length must be more then 2 and less then 100.")
    @Pattern(regexp = "^[\\p{L} .'-]+$", flags = Pattern.Flag.CASE_INSENSITIVE)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    @Size(min = 3, max = 500, message = "Description length must be more then 2 and less then 500.")
    @Pattern(regexp = "^[\\p{L} .'-]+$", flags = Pattern.Flag.CASE_INSENSITIVE, message = "")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Map<String, String> getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Map<String, String> thumbnail) {
        this.thumbnail = thumbnail;
    }

    @NotNull
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @NotNull
    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AddedPlace {
        private String placeId;
        private String sortKey;
        private Date createdDate;

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

        @Override
        public String toString() {
            return "AddedPlace{" +
                    "placeId='" + placeId + '\'' +
                    ", sortKey='" + sortKey + '\'' +
                    ", createdDate=" + createdDate +
                    '}';
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

    @Override
    public String toString() {
        return "PlaceCollection{" +
                "userId='" + userId + '\'' +
                ", collectionId='" + collectionId + '\'' +
                ", sortKey='" + sortKey + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", count=" + count +
                ", thumbnail=" + thumbnail +
                ", updatedDate=" + updatedDate +
                ", createdDate=" + createdDate +
                '}';
    }
}
