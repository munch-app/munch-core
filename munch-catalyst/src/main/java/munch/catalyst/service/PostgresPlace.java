package munch.catalyst.service;

import munch.struct.places.Place;

import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 6:57 PM
 * Project: munch-core
 */
public final class PostgresPlace {
    private String id;
    private Place place;

    private Date createdDate;
    private Date updatedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
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
}
