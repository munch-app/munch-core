package com.munch.core.struct.rdbms.place;

import com.munch.core.struct.rdbms.abs.AbsSortData;
import com.munch.core.struct.rdbms.locality.Country;
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
public class PlaceLocation extends AbsSortData {

    public static final int STATUS_ACTIVE = 200;
    public static final int STATUS_DELETED = 400;

    // Basic
    private String id;
    private String name;

    // Details
    private String phoneNumber;
    private Set<BusinessHour> businessHours;


    // Location
    private Double lat;
    private Double lng;

    private String address; // Address is everything below, (street, city, state, zip code and unit num are formatted data)

    private String block;
    private String town;
    private String street;
    private String zipCode;
    private String unitNumber;

    private String city;
    private String state;

    private Country country;

    // TODO Mall, Neigh?
    private String neighbourhood;

    // Data Tracking
    private int status = STATUS_ACTIVE;
    private int revision = 0;
    private String sourceUrl;

    // Backward Accessibility
    private Place place;

    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)")
    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(length = 255, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 45, nullable = true)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}, orphanRemoval = true)
    public Set<BusinessHour> getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(Set<BusinessHour> businessHours) {
        this.businessHours = businessHours;
    }

    @Column(nullable = false)
    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    @Column(nullable = false)
    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    @Column(length = 255, nullable = true)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(length = 100, nullable = true)
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Column(length = 30, nullable = true)
    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    @Column(length = 100, nullable = true)
    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    @Column(length = 60, nullable = true)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(length = 60, nullable = true)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(length = 20, nullable = true)
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Column(length = 70, nullable = true)
    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    /**
     * TODO
     */
    @Column(length = 70, nullable = true)
    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    @ManyToOne(optional = false)
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Column(nullable = false)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(nullable = false)
    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    /**
     * Basic Domain Name Only
     */
    @Column(length = 100, nullable = true)
    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String source) {
        this.sourceUrl = source;
    }

    @Transient
    public void incrementRevision() {
        setRevision(revision++);
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "placeId", nullable = false)
    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}
