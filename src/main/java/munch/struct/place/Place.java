package munch.struct.place;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 5:54 PM
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Place {

    private String id;
    // TODO version number?

    // Basic
    private String name;
    private String phone;
    private String website;
    private String description;

    // One
    private Price price;
    private Location location;

    // Many
    private Set<String> establishments;
    private Set<String> amenities;
    private Set<Image> images;
    private Set<Menu> menus;
    private Set<Hour> hours;

    // Dates
    private Date createdDate;
    private Date updatedDate;

    /**
     * @return place id, provided by catalyst groupId
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return name of the place, trim if over
     */
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
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return opening hours of place
     */
    public Set<Hour> getHours() {
        return hours;
    }

    public void setHours(Set<Hour> hours) {
        this.hours = hours;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Set<String> getEstablishments() {
        return establishments;
    }

    public void setEstablishments(Set<String> establishments) {
        this.establishments = establishments;
    }

    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public Set<Menu> getMenus() {
        return menus;
    }

    public void setMenus(Set<Menu> menus) {
        this.menus = menus;
    }

    /**
     * @return location of place
     */
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * @return amenities or type about a place
     */
    public Set<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(Set<String> amenities) {
        this.amenities = amenities;
    }

    /**
     * @return created date from catalyst group
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return updated date from catalyst group
     */
    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
