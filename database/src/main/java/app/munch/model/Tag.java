package app.munch.model;

import app.munch.model.constraint.TagDefaultGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.fuxing.err.ValidationException;
import dev.fuxing.utils.KeyUtils;
import dev.fuxing.validator.ValidEnum;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 2019-08-06
 * Time: 18:35
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "Tag")
public final class Tag {

    @NotNull
    @Pattern(regexp = "^[0-9a-hjkmnp-tv-z]{12}4$")
    @Id
    @Column(length = 13, updatable = false, nullable = false, unique = true)
    private String id;

    @ValidEnum
    @Enumerated(EnumType.STRING)
    @Column(length = 100, updatable = true, nullable = false, unique = false)
    private TagType type;

    @NotBlank
    @Column(length = 100, updatable = true, nullable = true, unique = false)
    private String name;

    /**
     * Date is tracked but it's json ignored when parsing
     */
    @JsonIgnore
    @NotNull(groups = TagDefaultGroup.class)
    @Version
    @Column(updatable = true, nullable = false)
    private Date updatedAt;

    /**
     * Date is tracked but it's json ignored when parsing
     */
    @JsonIgnore
    @NotNull(groups = TagDefaultGroup.class)
    @Column(updatable = false, nullable = false)
    private Date createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TagType getType() {
        return type;
    }

    public void setType(TagType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    private void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    private void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id) &&
                type == tag.type &&
                Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, name);
    }

    @PrePersist
    void prePersist() {
        setId(KeyUtils.nextL12() + "4");
        setCreatedAt(new Timestamp(System.currentTimeMillis()));

        preUpdate();
    }

    @PreUpdate
    void preUpdate() {
        setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        ValidationException.validate(this, Default.class, TagDefaultGroup.class);
    }

    /**
     * Null check is done for the set.
     * However null check is not done for each individual item in the set.
     *
     * @param left  Set of Tag lhs
     * @param right Set of Tag rhs
     * @return whether 2 set of Tag is the same
     */
    public static boolean equals(Set<Tag> left, Set<Tag> right) {
        if (left == null && right == null) return true;
        if (left == null || right == null) return false;

        Set<String> lhs = left.stream().map(Tag::getId)
                .collect(Collectors.toSet());
        Set<String> rhs = right.stream().map(Tag::getId)
                .collect(Collectors.toSet());

        return lhs.equals(rhs);
    }
}
