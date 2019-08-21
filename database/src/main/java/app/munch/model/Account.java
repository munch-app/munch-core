package app.munch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.fuxing.err.ValidationException;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.sql.Timestamp;
import java.util.Date;

/**
 * This data is directly mirroring Firebase Authentication for its id.
 * <p>
 * Created by: Fuxing
 * Date: 2019-07-15
 * Time: 22:58
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "Account")
public final class Account {

    /**
     * This is the only id not following the design guideline as it is taken from external source.
     */
    @NotNull
    @Id
    @Column(length = 255, updatable = false, nullable = false)
    private String id;

    @NotNull
    @Email
    @Column(length = 320, updatable = false, nullable = false)
    private String email;

    @OneToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY, optional = true, orphanRemoval = false)
    private Profile profile;

    @NotNull
    @Column(updatable = true, nullable = false)
    private Date authenticatedAt;

    @NotNull
    @Version
    @Column(updatable = true, nullable = false)
    private Date updatedAt;

    @NotNull
    @Column(updatable = false, nullable = false)
    private Date createdAt;

    /**
     * @return id, determine by external auth provider service.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id must be populated by auth provider.
     */
    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return {@link Profile}, might not exist if user haven't created their profile.
     */
    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Date getAuthenticatedAt() {
        return authenticatedAt;
    }

    public void setAuthenticatedAt(Date authenticatedAt) {
        this.authenticatedAt = authenticatedAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    void prePersist() {
        setCreatedAt(new Timestamp(System.currentTimeMillis()));
        preUpdate();
    }

    @PreUpdate
    void preUpdate() {
        setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        ValidationException.validate(this, Default.class);
    }
}
