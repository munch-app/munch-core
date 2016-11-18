package com.munch.core.struct.rdbms.locality;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created By: Fuxing Loh
 * Date: 28/9/2016
 * Time: 5:08 PM
 * Project: struct
 */
@Entity
public class Location {

    private String id;

    // Latitude & Longitude
    private Double lat;
    private Double lng;

    // Full address eg: 73 Ayer Rajah Crescent #03-27 Singapore 133952
    private String address;

    private String street;
    private String unitNumber;
    private String postal;

    private String city;
    private String state;

    private Country country;

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

    @Column(length = 255, nullable = false)
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

    @Column(length = 70, nullable = true)
    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unit) {
        this.unitNumber = unit;
    }

    @Column(length = 20, nullable = true)
    public String getPostal() {
        return postal;
    }

    public void setPostal(String zipCode) {
        this.postal = zipCode;
    }

    @Column(length = 70, nullable = true)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(length = 70, nullable = true)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
