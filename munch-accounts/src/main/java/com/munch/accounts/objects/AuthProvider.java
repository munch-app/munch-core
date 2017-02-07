package com.munch.accounts.objects;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Auth Provider provides alternative method for user to login
 * If there is no auth provider enable, make sure user password is not null
 * <p>
 * Created By: Fuxing Loh
 * Date: 5/2/2017
 * Time: 11:50 AM
 * Project: munch-core
 */
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"type", "userId"},
                name = "UK_AUTH_PROVIDER_TYPE_PROVIDER_ID"
        )
})
public final class AuthProvider {

    /**
     * Type of auth provider available
     * Static, Never change this values as @Enumerated(EnumType.STRING) is used
     * Only add new enum, never change existing values
     *
     * @see AuthProvider#getType()
     */
    public enum Type {
        /**
         * Facebook login
         */
        Facebook,

        /**
         * Instagram login
         */
        Instagram
    }

    private String id;
    private Type type;
    private String userId;

    private String accessToken;
    private String refreshToken;
    private String json;

    private Account account;

    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "CHAR(36)", nullable = false, updatable = false)
    @Id
    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    /**
     * For Facebook it is = userId
     * For Instagram it is = userId
     * <p>
     * To change providerId, hence account
     * The entire auth provider must be deleted
     *
     * @return provider id
     */
    @Column(length = 128, nullable = false, updatable = false)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String providerId) {
        this.userId = providerId;
    }

    @Column(length = 256, nullable = false, updatable = true)
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Column(length = 256, nullable = true)
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * json root must contains {"version": "1"} for version of data tracking
     *
     * @return Json data provided by the auth provider
     */
    @Column(length = 10_000, nullable = false)
    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    /**
     * @return account that own this auth provider
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
