package com.munch.struct;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 5:54 PM
 * Project: munch-core
 */
@Entity
public final class Place {

    private String id;

    private String name;
    private String phone;
    private String website;
    private String description;

    private Set<OpeningHours> openingHours;
    private Location location;

    private Date createdDate;
    private Date updatedDate;

    // TODO JsonB adaption

    /**
     * @return place id, provided by catalyst groupId
     */
    @Id
    @Column(columnDefinition = "CHAR(36)", nullable = false, updatable = false)
    public String getId() {
        return id;
    }

    /**
     * @param id from catalyst groupId
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return name of the place, trim if over
     */
    @Column(nullable = true, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Preferably with country code intact
     *
     * @return phone number of the place
     */
    @Column(nullable = true, length = 50)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Website url must not exceed 2000 character.
     * Trim if necessary
     * http://stackoverflow.com/questions/417142/what-is-the-maximum-length-of-a-url-in-different-browsers
     *
     * @return website url of place
     */
    @Column(nullable = true, length = 2000)
    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    /**
     * Description of place must not exceed 1000 character.
     * Trim if over 1000 characters
     *
     * @return description of place
     */
    @Column(nullable = true, length = 1000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return opening hours of place
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval = true)
    public Set<OpeningHours> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(Set<OpeningHours> openingHours) {
        this.openingHours = openingHours;
    }

    /**
     * @return location of place
     */
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval = true, optional = false)
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * @return created date from catalyst group
     */
    @Column(nullable = false)
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return updated date from catalyst group
     */
    @Column(nullable = false)
    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
