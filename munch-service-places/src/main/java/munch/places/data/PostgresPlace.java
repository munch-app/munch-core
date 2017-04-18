package munch.places.data;

import munch.places.data.hibernate.PlaceUserType;
import munch.struct.places.Place;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created By: Fuxing Loh
 * Date: 9/3/2017
 * Time: 2:33 PM
 * Project: munch-core
 */
@Entity
@TypeDef(name = "place", typeClass = PlaceUserType.class)
public final class PostgresPlace {

    private String id;
    private Place place;

    private Date createdDate;
    private Date updatedDate;

    public PostgresPlace() {/*For existing instance.*/}

    /**
     * For new entity instance
     *
     * @param key id from group
     */
    public PostgresPlace(String key) {
        this.id = key;
    }

    @Id
    @Column(columnDefinition = "CHAR(36)", nullable = false, updatable = false)
    public String getId() {
        return id;
    }

    protected void setId(String key) {
        this.id = key;
    }

    @Type(type = "place")
    @Column(nullable = true)
    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    @Column(nullable = false)
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Column(nullable = false)
    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
