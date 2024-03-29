package app.munch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.fuxing.err.ForbiddenException;
import dev.fuxing.err.ValidationException;
import dev.fuxing.utils.KeyUtils;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.Length;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 2019-07-14
 * Time: 14:45
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "Profile")
public final class Profile implements ElasticSerializable {

    /**
     * Admin Id must be used for internal resources with no public facing entities<br>
     */
    public static final String ADMIN_ID = "00000000000000000000000000";

    /**
     * Backward compatible support, user different id for each major deprecation so that it's easier to delete.
     */
    public static final String COMPAT_ID = "000000000000000000v22t0v23";

    /**
     * All special uid.
     */
    public static final Set<String> ALL_SPECIAL_ID = Set.of(
            ADMIN_ID, COMPAT_ID
    );

    @NotNull
    @Pattern(regexp = KeyUtils.ULID_REGEX)
    @Id
    @Column(length = 26, updatable = false, nullable = false, unique = true)
    private String uid;

    /**
     * In the future, username renaming must be limited and username can only be used after 1 month of inactivity.
     */
    @NotNull
    @Pattern(regexp = "[a-z0-9]{3,64}")
    @Column(length = 255, updatable = true, nullable = false, unique = true)
    private String username;

    @NotBlank
    @Length(max = 100)
    @Column(length = 100, updatable = true, nullable = true, unique = false)
    private String name;

    @Length(max = 250)
    @Column(length = 250, updatable = true, nullable = true, unique = false)
    private String bio;

    @ManyToOne(cascade = {}, fetch = FetchType.EAGER, optional = true)
    private Image image;

    @Size(max = 4)
    @OrderBy("position DESC")
    @LazyCollection(LazyCollectionOption.TRUE)
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "profile", orphanRemoval = true)
    private List<ProfileLink> links;

    @NotNull
    @Version
    @Column(updatable = true, nullable = false, unique = false)
    private Date updatedAt;

    @NotNull
    @Column(updatable = false, nullable = false, unique = false)
    private Date createdAt;

    public String getUid() {
        return uid;
    }

    public void setUid(String id) {
        this.uid = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<ProfileLink> getLinks() {
        return Lazy.load(links, null);
    }

    public void setLinks(List<ProfileLink> links) {
        this.links = links;
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
        long millis = System.currentTimeMillis();
        if (getUid() == null) {
            setUid(KeyUtils.nextULID(millis));
        }
        setCreatedAt(new Timestamp(millis));

        if (getUsername() == null) {
            setUsername(getUid());
        }

        preUpdate();
    }

    @PreUpdate
    void preUpdate() {
        setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        if (getLinks() != null) {
            getLinks().forEach(link -> {
                link.setProfile(this);
            });
        }

        ValidationException.validate(this, Default.class);
    }

    @Override
    public ElasticDocumentType getElasticDocumentType() {
        return ElasticDocumentType.PROFILE;
    }

    @Override
    public Map<String, String> getElasticDocumentKeys() {
        return Map.of("uid", getUid());
    }

    /**
     * @param entityManager to use
     * @param accountId     to match
     * @param profile       to match
     * @throws ForbiddenException if not authorized
     */
    public static void authorize(EntityManager entityManager, String accountId, Profile profile) {
        authorize(profile, findByAccountId(entityManager, accountId));
    }

    public static void authorize(Profile left, Profile right) {
        if (left == null) throw new ForbiddenException();
        if (right == null) throw new ForbiddenException();

        if (!left.getUid().equals(right.getUid())) {
            throw new ForbiddenException();
        }
    }

    /**
     * Helper method to find Profile by Account.id
     *
     * @param entityManager to use
     * @param accountId     to map to Profile
     * @return Profile
     */
    @Nullable
    public static Profile findByAccountId(EntityManager entityManager, String accountId) {
        List<Profile> list = entityManager.createQuery("SELECT a.profile FROM Account a " +
                "WHERE a.id = :id", Profile.class)
                .setParameter("id", accountId)
                .setMaxResults(1)
                .getResultList();

        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * Helper method to find Profile by Profile.uid
     *
     * @param entityManager to use
     * @param uid           to map to Profile
     * @return Profile
     */
    @Nullable
    public static Profile findByUid(EntityManager entityManager, String uid) {
        return entityManager.find(Profile.class, uid);
    }

    /**
     * Helper method to find Profile by username
     *
     * @param entityManager to use
     * @param username      to map to Profile
     * @return Profile
     */
    @Nullable
    public static Profile findByUsername(EntityManager entityManager, String username) {
        List<Profile> list = entityManager.createQuery("FROM Profile " +
                "WHERE username = :username", Profile.class)
                .setParameter("username", username)
                .setMaxResults(1)
                .getResultList();

        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * Profile Supplier
     */
    @FunctionalInterface
    public interface Supplier {
        Profile supply(EntityManager entityManager);
    }

    /**
     * @param entityManager jpa entity manager
     * @return attached Profile
     */
    public Profile attach(EntityManager entityManager) {
        return entityManager.find(Profile.class, getUid());
    }
}
