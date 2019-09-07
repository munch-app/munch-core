package app.munch.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.err.ValidationException;
import dev.fuxing.postgres.JsonUserType;
import dev.fuxing.utils.KeyUtils;
import dev.fuxing.validator.ValidEnum;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 30/8/19
 * Time: 12:19 PM
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "WorkerTask")
@TypeDef(name = "JsonUserType", typeClass = JsonUserType.class)
public final class WorkerTask {

    @NotNull
    @Pattern(regexp = KeyUtils.ULID_REGEX)
    @Id
    @Column(length = 26, updatable = false, nullable = false, unique = true)
    private String uid;

    @NotNull
    @JsonIgnore
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = false)
    private WorkerGroup group;

    @ValidEnum
    @Enumerated(EnumType.STRING)
    @Column(length = 100, updatable = true, nullable = false, unique = false)
    private WorkerTaskStatus status;

    @NotNull
    @Column(updatable = false, nullable = false, unique = false)
    private Date startedAt;

    @NotNull
    @Column(updatable = true, nullable = false, unique = false)
    private Date updatedAt;

    @Column(updatable = false, nullable = true, unique = false)
    private Date completedAt;

    @Type(type = "JsonUserType")
    private JsonNode details;

    public String getUid() {
        return uid;
    }

    private void setUid(String uid) {
        this.uid = uid;
    }

    public WorkerGroup getGroup() {
        return group;
    }

    public void setGroup(WorkerGroup group) {
        this.group = group;
    }

    public WorkerTaskStatus getStatus() {
        return status;
    }

    public void setStatus(WorkerTaskStatus status) {
        this.status = status;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    private void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    private void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    private void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    public JsonNode getDetails() {
        return details;
    }

    public void setDetails(JsonNode details) {
        this.details = details;
    }

    @PrePersist
    void prePersist() {
        long millis = System.currentTimeMillis();
        setUid(KeyUtils.nextULID(millis));
        setStartedAt(new Timestamp(millis));

        preUpdate();
    }

    @PreUpdate
    void preUpdate() {
        if (getStatus() == WorkerTaskStatus.COMPLETED) {
            setCompletedAt(new Timestamp(System.currentTimeMillis()));
        }

        setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        ValidationException.validate(this, Default.class);
    }
}
