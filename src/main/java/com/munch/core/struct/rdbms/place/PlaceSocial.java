package com.munch.core.struct.rdbms.place;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created By: Fuxing Loh
 * Date: 1/10/2016
 * Time: 5:12 PM
 * Project: struct
 */
@Entity
public class PlaceSocial {

    private String id;

    // Social Link
    private String facebookPageId;
    private String facebookPlaceId; // Unique To Location
    private String googlePlaceId; // Unique To Location
    private String instagramUserId;
    private String instagramPlaceId; // Unique To Location

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

    @Column(nullable = true, length = 100)
    public String getFacebookPageId() {
        return facebookPageId;
    }

    public void setFacebookPageId(String facebookPageId) {
        this.facebookPageId = facebookPageId;
    }

    @Column(nullable = true, length = 100)
    public String getFacebookPlaceId() {
        return facebookPlaceId;
    }

    public void setFacebookPlaceId(String facebookPlaceId) {
        this.facebookPlaceId = facebookPlaceId;
    }

    @Column(nullable = true, length = 100)
    public String getGooglePlaceId() {
        return googlePlaceId;
    }

    public void setGooglePlaceId(String googlePlaceId) {
        this.googlePlaceId = googlePlaceId;
    }

    @Column(nullable = true, length = 100)
    public String getInstagramUserId() {
        return instagramUserId;
    }

    public void setInstagramUserId(String instagramUserId) {
        this.instagramUserId = instagramUserId;
    }

    @Column(nullable = true, length = 100)
    public String getInstagramPlaceId() {
        return instagramPlaceId;
    }

    public void setInstagramPlaceId(String instagramPlaceId) {
        this.instagramPlaceId = instagramPlaceId;
    }
}
