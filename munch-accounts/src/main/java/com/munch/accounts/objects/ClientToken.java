package com.munch.accounts.objects;

import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 6/2/2017
 * Time: 9:37 PM
 * Project: munch-core
 */
@Entity
public final class ClientToken {
    private static final SecureRandom RANDOM = new SecureRandom();

    private String token;
    private String roles;

    private String name;
    private String device;
    private String ipAddress;

    private Date lastAccessed;
    private Date createdDate;
    private Date updatedDate;

    private Account account;

    @PrePersist
    protected void onCreate() {
        setCreatedDate(new Timestamp(System.currentTimeMillis()));
        setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        setLastAccessed(new Timestamp(System.currentTimeMillis()));
        setToken(RandomStringUtils.random(255, 0, 0, true, true, null, RANDOM));
    }

    @PreUpdate
    protected void onUpdate() {
        setUpdatedDate(new Timestamp(System.currentTimeMillis()));
    }

    /**
     * Length 255, letters and numbers generated with secure random
     *
     * @return token of client
     */
    @Id
    @Column(length = 255, nullable = false, updatable = false)
    public String getToken() {
        return token;
    }

    protected void setToken(String token) {
        this.token = token;
    }

    /**
     * Roles separated by comma
     *
     * @return roles client is allowed to act on
     */
    @Column(length = 255, nullable = false)
    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    /**
     * Name of client set by the user
     *
     * @return name of the client
     */
    @Column(length = 150, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Device name can be iOS, Browser, Android
     *
     * @return device name of client
     */
    @Column(length = 50, nullable = false)
    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    /**
     * @return ip Address where client is first created
     */
    @Column(length = 50, nullable = false, updatable = false)
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * @return date where client is last accessed
     */
    @Column(nullable = false)
    public Date getLastAccessed() {
        return lastAccessed;
    }

    public void setLastAccessed(Date lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

    /**
     * @return date where client is created
     */
    @Column(nullable = false)
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return date where client info is edited
     */
    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * @return account that own this token
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "ClientToken{" +
                "token='" + token + '\'' +
                ", roles='" + roles + '\'' +
                ", name='" + name + '\'' +
                ", device='" + device + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", lastAccessed=" + lastAccessed +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", account=" + account +
                '}';
    }
}
