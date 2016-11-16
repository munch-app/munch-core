package com.munch.core.struct.rdbms.place;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.munch.core.struct.rdbms.abs.AbsAuditData;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 11/10/2016
 * Time: 2:50 AM
 * Project: struct
 */
@Entity
public class Brand extends AbsAuditData {

    private String id;

    private String name;
    private String description;

    private String phoneNumber;
    private String websiteUrl;

    private Set<Place> places = new HashSet<>();

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

    @Column(length = 1000, nullable = true)
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

    @Column(length = 255, nullable = true)
    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST}, orphanRemoval = false, mappedBy = "brand")
    @JsonManagedReference
    public Set<Place> getPlaces() {
        return places;
    }

    protected void setPlaces(Set<Place> places) {
        this.places = places;
    }
}
