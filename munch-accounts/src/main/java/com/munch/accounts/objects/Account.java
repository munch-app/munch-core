package com.munch.accounts.objects;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 4/2/2017
 * Time: 6:11 PM
 * Project: munch-core
 */
@Entity
public final class Account {

    // Identity Info
    private String accountId;
    private String email;
    private String password;
    private String phone;
    private Country country;

    private boolean emailVerified = false;
    private boolean phoneVerified = false;

    // Personal Info
    private String firstName; // Given Name
    private String lastName; // Surname
    private String pictureUrl;
    private String bio;
    private Date birthDate; // Date of birth
    private Gender gender;

    private Date createdDate;
    private Date updatedDate;
    private Date lastLoginDate;

    private Set<ClientToken> tokens = new HashSet<>();
    private Set<AuthProvider> providers = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        setCreatedDate(new Timestamp(System.currentTimeMillis()));
        setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        setLastLoginDate(new Timestamp(System.currentTimeMillis()));
    }

    @PreUpdate
    protected void onUpdate() {
        setUpdatedDate(new Timestamp(System.currentTimeMillis()));

        // Set phone verified to false is blank
        if (StringUtils.isBlank(getPhone()) && isPhoneVerified()) {
            setPhoneVerified(false);
        }
    }

    /**
     * @return unique account id for munch
     */
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "CHAR(36)", nullable = false, updatable = false)
    @Id
    public String getAccountId() {
        return accountId;
    }

    protected void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * @return email of account, must be unique
     */
    @Column(length = 255, nullable = false, unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Optional, if null means password is not enable, must use auth provider to login
     *
     * @return SHA-256 hashed password
     */
    @Column(columnDefinition = "CHAR(64)", nullable = true)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return phone number of the person in full, with iso
     */
    @Column(length = 50, nullable = true)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return country in which account is created at
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    /**
     * @return true if email is verified
     */
    @Column(nullable = false)
    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    /**
     * If phone number is blank on persist, it will be set to false by default
     *
     * @return true if phone is verified
     */
    @Column(nullable = false)
    public boolean isPhoneVerified() {
        return phoneVerified;
    }

    public void setPhoneVerified(boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    /**
     * @return firstName of person aka: givenName
     */
    @Column(nullable = false, length = 100)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return lastName of person aka: surname
     */
    @Column(nullable = true, length = 50)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    /**
     * @return public url of person profile image
     */
    @Column(length = 500, nullable = false)
    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    /**
     * @return short bio biography of a person
     */
    @Column(nullable = true, length = 200)
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * @return user date of birth
     */
    @Column(nullable = false)
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * @return Gender enum
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public Gender getGender() {
        return gender;
    }

    /**
     * @param gender enum of gender
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * @return date account is created
     */
    @Column(nullable = false, updatable = false)
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return date account details is updated
     */
    @Column(nullable = false)
    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * To check last activity, look at all the client token last activity
     *
     * @return date of last login to create token
     */
    @Column(nullable = false)
    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true, mappedBy = "account")
    public Set<ClientToken> getTokens() {
        return tokens;
    }

    public void setTokens(Set<ClientToken> tokens) {
        this.tokens = tokens;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true, mappedBy = "account")
    public Set<AuthProvider> getProviders() {
        return providers;
    }

    public void setProviders(Set<AuthProvider> providers) {
        this.providers = providers;
    }
}
