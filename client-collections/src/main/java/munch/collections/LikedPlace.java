package munch.collections;

import java.util.Date;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 9/1/18
 * Time: 4:29 PM
 * Project: munch-core
 */
public final class LikedPlace {
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
        LikedPlace that = (LikedPlace) o;
        return Objects.equals(placeId, that.placeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId);
    }
}
