package com.munch.core.struct.rdbms.admin;

import com.munch.core.essential.util.DateTime;
import com.munch.core.struct.rdbms.abs.AbsSortData;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created By: Fuxing Loh
 * Date: 26/8/2016
 * Time: 11:12 PM
 * Project: struct
 */
@Entity
public class AdminAccount extends AbsSortData {

    // High level access
    public static final int TYPE_ADMIN = 200;
    public static final int TYPE_FOUNDER = 201;

    public static final int TYPE_EMPLOYEE = 300;
    public static final int TYPE_CONTACT_EMPLOYEE = 400;

    public static final int TYPE_BOT = 1000;

    private String id;
    private int type;

    // Information
    private String name;
    private String phoneNumber;

    // For Login
    private String email;
    private String hashSaltPassword;
    // Integrate google sign in with domain check in the future.
    // For contact worker, phone number with password & code? devise something for them
    private String secretToken;

    private Date lastActiveDate;
    private Date lastLoginDate;

    /**
     * Default pre persist time stamping
     */
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        setLastLoginDate(new Timestamp(DateTime.millisNow()));
        setLastActiveDate(new Timestamp(DateTime.millisNow()));
        setSecretToken(RandomStringUtils.randomAlphanumeric(200));
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

    @Column
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Column(length = 255, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 45)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(length = 255, nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(length = 200, nullable = false)
    public String getSecretToken() {
        return secretToken;
    }

    public void setSecretToken(String secretToken) {
        this.secretToken = secretToken;
    }

    @Column(length = 100)
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

    public void updateLastLoginDate() {
        updateLastActiveDate();
        setLastLoginDate(DateTime.now());
    }

    public void updateLastActiveDate() {
        setLastActiveDate(DateTime.now());
    }
}
