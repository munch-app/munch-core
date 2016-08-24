package com.munch.core.struct.database.account;

import com.munch.core.struct.database.abs.AbsAuditData;
import com.munch.core.struct.database.type.Country;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 24/8/2016
 * Time: 2:57 AM
 * Project: struct
 */
public class Account extends AbsAuditData {

    public static final int TYPE_NORMAL = 10_000;
    public static final int TYPE_RESTURANT = 12_000;
    public static final int TYPE_INFLUENCER = 15_000;

    // Crucial Info
    private String id;
    private String email;
    private String phoneCountry;
    private String phoneNumber;
    private Country country;
    private int type;

    // Personal Info
    private String firstName; // Or name
    private String lastName;
    private String bio;

    // Associate Accounts
    private String facebookUserId;
    private String instagramUserId;
    private String hashSaltPassword;

    // Tracking
    private Date lastActiveDate;
    private Date lastLoginDate;
    private boolean emailVerified;
    private boolean phoneVerified;

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

    @Column(nullable = false, length = 255, unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneCountry() {
        return phoneCountry;
    }

    public void setPhoneCountry(String phoneCountry) {
        this.phoneCountry = phoneCountry;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getFacebookUserId() {
        return facebookUserId;
    }

    public void setFacebookUserId(String facebookUserId) {
        this.facebookUserId = facebookUserId;
    }

    public String getInstagramUserId() {
        return instagramUserId;
    }

    public void setInstagramUserId(String instagramUserId) {
        this.instagramUserId = instagramUserId;
    }

    public String getHashSaltPassword() {
        return hashSaltPassword;
    }

    public void setHashSaltPassword(String hashSaltPassword) {
        this.hashSaltPassword = hashSaltPassword;
    }

    public Date getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(Date lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isPhoneVerified() {
        return phoneVerified;
    }

    public void setPhoneVerified(boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }
}
