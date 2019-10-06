package app.munch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.fuxing.utils.KeyUtils;
import dev.fuxing.validator.ValidEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @author Fuxing Loh
 * @since 6/10/19 at 5:27 pm
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "UpdateLocking", uniqueConstraints = {
        @UniqueConstraint(name = "uk_aqgc0bg0fbv1ftp6", columnNames = {"type", "id"}),
})
public final class UpdateLocking {

    @NotNull
    @Pattern(regexp = KeyUtils.ULID_REGEX)
    @Id
    @Column(length = 26, updatable = false, nullable = false, unique = true)
    private String uid;

    @ValidEnum
    @Enumerated(EnumType.STRING)
    @Column(length = 100, updatable = true, nullable = false, unique = false)
    private UpdateLockingType type;

    @NotNull
    @Column(length = 255, updatable = false, nullable = false)
    private String id;

    @ValidEnum
    @Enumerated(EnumType.STRING)
    @Column(length = 100, updatable = true, nullable = false, unique = false)
    private UpdateLockingLevel level;

    @NotNull
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = false)
    private Profile createdBy;

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

    public void setUid(String uid) {
        this.uid = uid;
    }

    public UpdateLockingType getType() {
        return type;
    }

    public void setType(UpdateLockingType type) {
        this.type = type;
    }

    public UpdateLockingLevel getLevel() {
        return level;
    }

    public void setLevel(UpdateLockingLevel level) {
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Profile getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Profile createdBy) {
        this.createdBy = createdBy;
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
}
