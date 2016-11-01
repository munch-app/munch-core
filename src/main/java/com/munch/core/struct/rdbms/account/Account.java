package com.munch.core.struct.rdbms.account;

import com.munch.core.essential.util.DateTime;
import com.munch.core.struct.rdbms.abs.AbsAuditData;
import com.munch.core.struct.rdbms.locality.Country;
import com.munch.core.struct.util.map.BiHashSet;
import com.munch.core.struct.util.map.OneEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

/**
 * @deprecated not ready to use yet
 * Created by: Fuxing
 * Date: 24/8/2016
 * Time: 2:57 AM
 * Project: struct
 */
@Entity
@Table(indexes = {@Index(name = "EMAIL_INDEX", columnList = "email"), @Index(name = "PHONE_INDEX", columnList = "phoneNumber")})
@Deprecated
public class Account extends AbsAuditData implements OneEntity {

    public static final int TYPE_NORMAL = 10_000;
    public static final int TYPE_RESTURANT = 12_000;
    public static final int TYPE_INFLUENCER = 15_000;

    // Crucial Info
    private String id;
    private String email;
    private String phoneNumber;
    private Country country;
    private Integer type; // Cannot be null

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
    private boolean emailVerified = false;
    private boolean phoneVerified = false;
    private Set<AccountAccessToken> accessTokens = new BiHashSet<>(this);

    /**
     * Default pre persist time stamping
     */
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        setLastLoginDate(new Timestamp(DateTime.millisNow()));
        setLastActiveDate(new Timestamp(DateTime.millisNow()));
    }

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

    @Column(nullable = false, length = 255)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(nullable = true, length = 45)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Column(nullable = false)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(nullable = false, length = 100)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(nullable = true, length = 50)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(nullable = true, length = 255)
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Column(nullable = true, length = 100)
    public String getFacebookUserId() {
        return facebookUserId;
    }

    public void setFacebookUserId(String facebookUserId) {
        this.facebookUserId = facebookUserId;
    }

    @Column(nullable = true, length = 100)
    public String getInstagramUserId() {
        return instagramUserId;
    }

    public void setInstagramUserId(String instagramUserId) {
        this.instagramUserId = instagramUserId;
    }

    @Column(nullable = true, length = 100)
    public String getHashSaltPassword() {
        return hashSaltPassword;
    }

    public void setHashSaltPassword(String hashSaltPassword) {
        this.hashSaltPassword = hashSaltPassword;
    }

    @Column(nullable = false)
    public Date getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(Date lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    @Column(nullable = false)
    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    @Column
    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    @Column
    public boolean isPhoneVerified() {
        return phoneVerified;
    }

    public void setPhoneVerified(boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "account")
    public Set<AccountAccessToken> getAccessTokens() {
        return accessTokens;
    }

    protected void setAccessTokens(Set<AccountAccessToken> accessTokens) {
        this.accessTokens = accessTokens;
    }
}
