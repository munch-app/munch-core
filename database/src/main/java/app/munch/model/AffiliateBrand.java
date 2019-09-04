package app.munch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.fuxing.err.ValidationException;
import dev.fuxing.utils.KeyUtils;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;

/**
 * Created by: Fuxing
 * Date: 3/9/19
 * Time: 7:13 pm
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "AffiliateBrand")
public final class AffiliateBrand {

    @NotNull
    @Pattern(regexp = KeyUtils.ULID_REGEX)
    @Id
    @Column(length = 26, updatable = false, nullable = false, unique = true)
    private String uid;

    @NotBlank
    @Length(max = 100)
    @Column(length = 100, updatable = true, nullable = false, unique = false)
    private String name;

    @NotNull
    @ManyToOne(cascade = {}, fetch = FetchType.EAGER, optional = false)
    private Image image;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @PrePersist
    void prePersist() {
        setUid(KeyUtils.nextULID());

        preUpdate();
    }

    @PreUpdate
    void preUpdate() {
        ValidationException.validate(this, Default.class);
    }
}
