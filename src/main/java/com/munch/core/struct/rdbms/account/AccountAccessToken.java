package com.munch.core.struct.rdbms.account;

import com.munch.core.struct.rdbms.abs.AbsAuditData;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;

/**
 * Created by: Fuxing
 * Date: 24/8/2016
 * Time: 3:19 AM
 * Project: struct
 */
@Entity
public class AccountAccessToken extends AbsAuditData {

    public static final int TYPE_IOS = 20_000;
    public static final int TYPE_ANDROID = 21_000;
    public static final int TYPE_WEBSITE = 22_000;

    private String accessKey;
    private String secretKey;
    private Integer type;

    // Description
    private String uid;
    private String description;

    private Account account;

    /**
     * Auto generate access key and secret key
     */
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        accessKey = RandomStringUtils.randomAlphanumeric(50);
        secretKey = RandomStringUtils.randomAlphanumeric(100);
    }

    @Id
    @Column(unique = true, nullable = false, length = 50)
    public String getAccessKey() {
        return accessKey;
    }

    protected void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    @Column(nullable = false, length = 100)
    public String getSecretKey() {
        return secretKey;
    }

    protected void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Column(nullable = false)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(nullable = false, length = 80)
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Column(nullable = false, length = 80)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
