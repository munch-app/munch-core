package munch.collections;

import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 9/1/18
 * Time: 4:29 PM
 * Project: munch-core
 */
public final class AddedPlace {
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
}
