package com.munch.core.struct.rdbms.locality;

import com.munch.core.struct.rdbms.abs.AbsSortData;
import com.munch.core.struct.rdbms.media.SortedImage;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 25/9/2016
 * Time: 11:41 PM
 * Project: struct
 */
@Entity
public class Neighborhood extends AbsSortData {

    private String id;

    private String name;
    private String description;
    private String websiteUrl;

    private Location location;
    private Double radius; // In KM

    private List<SortedImage> images = new ArrayList<>();

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

    @Column(length = 512, nullable = true)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(length = 255, nullable = true)
    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, optional = false, orphanRemoval = true)
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Column(nullable = true)
    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
    @OrderBy("sort desc")
    @LazyCollection(LazyCollectionOption.FALSE)
    public List<SortedImage> getImages() {
        return images;
    }

    protected void setImages(List<SortedImage> images) {
        this.images = images;
    }
}
