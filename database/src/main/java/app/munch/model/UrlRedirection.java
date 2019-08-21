package app.munch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.fuxing.err.ValidationException;
import dev.fuxing.utils.KeyUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 20/8/19
 * Time: 11:32 pm
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "UrlRedirection")
public final class UrlRedirection {

    @NotNull
    @Pattern(regexp = KeyUtils.ULID_REGEX)
    @Id
    @Column(length = 26, updatable = false, nullable = false, unique = true)
    private String uid;

    @NotNull
    @Pattern(regexp = "^/.{1,999}$")
    @Column(length = 1000, updatable = false, nullable = false, unique = true)
    private String pathFrom;

    @NotNull
    @Pattern(regexp = "^/.{1,999}$")
    @Column(length = 1000, updatable = true, nullable = false, unique = false)
    private String pathTo;

    @Column(length = 1000, updatable = true, nullable = true, unique = false)
    private String description;

    @NotNull
    @Version
    @Column(updatable = true, nullable = false)
    private Date updatedAt;

    @NotNull
    @Column(updatable = false, nullable = false)
    private Date createdAt;

    public String getUid() {
        return uid;
    }

    public void setUid(String id) {
        this.uid = id;
    }

    public String getPathFrom() {
        return pathFrom;
    }

    public void setPathFrom(String from) {
        this.pathFrom = from;
    }

    public String getPathTo() {
        return pathTo;
    }

    public void setPathTo(String to) {
        this.pathTo = to;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        setUid(KeyUtils.nextULID());
        setCreatedAt(new Timestamp(System.currentTimeMillis()));

        preUpdate();
    }

    @PreUpdate
    void preUpdate() {
        setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        ValidationException.validate(this, Default.class);
    }
}
