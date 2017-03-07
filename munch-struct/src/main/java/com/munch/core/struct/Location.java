package com.munch.core.struct;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 7:10 PM
 * Project: munch-core
 */
@Entity
public final class Location {

    private String id;

    private String address;
    private String unitNumber;

    private String city;
    private String country;

    private String postal;
    private Double lat;
    private Double lng;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "CHAR(36)", nullable = false, updatable = false)
    @JsonIgnore
    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    /**
     * @return address in full
     */
    @Column(nullable = false, length = 255)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Max 20 characters. Force trim if necessary
     *
     * @return unit number of location
     */
    @Column(nullable = true, length = 20)
    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    /**
     * @return city of location
     */
    @Column(nullable = false, length = 255)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return country of location
     */
    @Column(nullable = false, length = 255)
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Max length, 20 characters.
     * https://en.wikipedia.org/wiki/Postal_code
     *
     * @return postal code of the location
     */
    @Column(nullable = false, length = 20)
    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    /**
     * @return latitude of the location
     */
    @Column(nullable = false)
    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    /**
     * @return longitude of the location
     */
    @Column(nullable = false)
    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
