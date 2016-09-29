package com.munch.core.struct.rdbms.place;

import com.munch.core.struct.rdbms.abs.AbsSortData;
import com.munch.core.struct.rdbms.abs.HashSetData;
import com.munch.core.struct.rdbms.locality.Container;
import com.munch.core.struct.rdbms.locality.Location;
import com.munch.core.struct.rdbms.locality.Neighborhood;
import com.munch.core.struct.util.map.BiDirectionHashSet;
import com.munch.core.struct.util.map.ManyEntity;
import com.munch.core.struct.util.map.OneEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

/**
 * Created By: Fuxing Loh
 * Date: 23/8/2016
 * Time: 4:16 PM
 * Project: struct
 */
@Entity
public class PlaceLocation extends AbsSortData implements HashSetData, ManyEntity<Place>, OneEntity {

    public static final int STATUS_ACTIVE = 200;
    public static final int STATUS_DELETED = 400;

    // Basic
    private String id;
    private String name;
    private String description;

    // Details
    private String phoneNumber;
    private Set<PlaceHour> placeHours = new BiDirectionHashSet<>(this);

    // Location Data
    private Location location;
    private Neighborhood neighbourhood;
    private Container container;

    // Backward Accessibility
    private Place place;

    // Data Tracking
    private int status = STATUS_ACTIVE;

    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)")
    @Id
    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    @Column(length = 255, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 500, nullable = true)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(length = 45, nullable = true)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval = true, mappedBy = "placeLocation")
    public Set<PlaceHour> getPlaceHours() {
        return placeHours;
    }

    protected void setPlaceHours(Set<PlaceHour> placeHours) {
        this.placeHours = placeHours;
    }

    @Column(nullable = false)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @OneToOne(cascade = {CascadeType.ALL}, optional = false, orphanRemoval = true)
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @ManyToOne(optional = true)
    public Neighborhood getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(Neighborhood neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    @ManyToOne(optional = true)
    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public Place getPlace() {
        return place;
    }

    protected void setPlace(Place place) {
        this.place = place;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return equals(obj, getClass());
    }

    @Override
    public void setOneEntity(Place single) {
        setPlace(single);
    }
}
