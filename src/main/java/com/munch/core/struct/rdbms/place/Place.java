package com.munch.core.struct.rdbms.place;

import com.munch.core.struct.rdbms.abs.AbsAuditData;
import com.munch.core.struct.rdbms.menu.MenuMedia;
import com.munch.core.struct.rdbms.menu.MenuWebsite;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

/**
 * Created By: Fuxing Loh
 * Date: 23/8/2016
 * Time: 7:35 PM
 * Project: struct
 */
@Entity
public class Place extends AbsAuditData {

    // Basic
    private String id;
    private String name;
    private String description;

    // Informational
    private String phoneNumber;
    private String websiteUrl;
    private Set<PlaceType> types;
    private Set<PlaceTag> tags;

    // Details
    private double priceStart;
    private double priceEnd;

    // Menu
    private Set<MenuWebsite> menuWebsites;
    private Set<MenuMedia> menuMedias;

    // Related
    private Set<PlaceLocation> locations;
    private Set<PlaceMedia> medias;

    // Data Tracking
    private int status;
    private int revision;
    private String sourceUrl;

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

    @Column(length = 255, nullable = true)
    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    public Set<PlaceType> getTypes() {
        return types;
    }

    public void setTypes(Set<PlaceType> types) {
        this.types = types;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}, orphanRemoval = true)
    public Set<PlaceTag> getTags() {
        return tags;
    }

    public void setTags(Set<PlaceTag> tags) {
        this.tags = tags;
    }

    @Column
    public double getPriceStart() {
        return priceStart;
    }

    public void setPriceStart(double priceStart) {
        this.priceStart = priceStart;
    }

    @Column
    public double getPriceEnd() {
        return priceEnd;
    }

    public void setPriceEnd(double priceEnd) {
        this.priceEnd = priceEnd;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}, orphanRemoval = true)
    public Set<MenuWebsite> getMenuWebsites() {
        return menuWebsites;
    }

    public void setMenuWebsites(Set<MenuWebsite> menuWebsites) {
        this.menuWebsites = menuWebsites;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}, orphanRemoval = true)
    public Set<MenuMedia> getMenuMedias() {
        return menuMedias;
    }

    public void setMenuMedias(Set<MenuMedia> menuMedias) {
        this.menuMedias = menuMedias;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}, orphanRemoval = true)
    public Set<PlaceLocation> getLocations() {
        return locations;
    }

    public void setLocations(Set<PlaceLocation> locations) {
        this.locations = locations;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}, orphanRemoval = true)
    public Set<PlaceMedia> getMedias() {
        return medias;
    }

    public void setMedias(Set<PlaceMedia> medias) {
        this.medias = medias;
    }

    @Column
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column
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
}
