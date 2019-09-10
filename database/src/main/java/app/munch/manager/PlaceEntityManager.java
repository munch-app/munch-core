package app.munch.manager;

import app.munch.exception.PlaceLockedException;
import app.munch.exception.RestrictionException;
import app.munch.model.*;
import dev.fuxing.jpa.HibernateUtils;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.utils.JsonUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/**
 * Created by: Fuxing
 * Date: 31/8/19
 * Time: 4:46 pm
 */
@Singleton
public final class PlaceEntityManager {
    /*
     * SQS -> Elastic for indexing (Decoupling)
     * EntityManager will have no direct access to Elastic Index
     */

    private final TransactionProvider transactionProvider;

    @Inject
    PlaceEntityManager(TransactionProvider transactionProvider) {
        this.transactionProvider = transactionProvider;
    }

    public Place get(String id) {
        return transactionProvider.reduce(true, entityManager -> {
            Place place = entityManager.find(Place.class, id);
            HibernateUtils.initialize(place.getImage());
            HibernateUtils.initialize(place.getCreatedBy());
            return place;
        });
    }

    /**
     * @param model           to create the place data
     * @param editableFields  fields that are editable
     * @param profileProvider profile used for creating the place
     * @return Created Place
     * @throws RestrictionException unable to make changes due to account restriction
     */
    public Place create(PlaceModel model, Set<PlaceEditableField> editableFields, Function<EntityManager, Profile> profileProvider) throws RestrictionException {
        return transactionProvider.reduce(entityManager -> {
            Profile profile = profileProvider.apply(entityManager);
            return publish(entityManager, profile.getUid(), null, model, editableFields);
        });
    }

    /**
     * Revision will only be created if there is change in data
     *
     * @param id              of the existing place to mutate
     * @param model           to write/mutate into existing place
     * @param editableFields  fields that are editable
     * @param profileProvider profile used for creating the revision
     * @return Mutated Place, (Will not mutate if no changes)
     * @throws RestrictionException unable to make changes due to account restriction
     * @throws PlaceLockedException unable to make changes due to place locking mechanism
     */
    public Place update(String id, PlaceModel model, Set<PlaceEditableField> editableFields, Function<EntityManager, Profile> profileProvider) throws RestrictionException, PlaceLockedException {
        Objects.requireNonNull(id);
        Objects.requireNonNull(model);

        return transactionProvider.reduce(entityManager -> {
            Profile profile = profileProvider.apply(entityManager);
            return publish(entityManager, profile.getUid(), id, model, editableFields);
        });
    }

    /**
     * This method is different from the about, it will not call terminate transaction or rollback
     * instead a exception is just thrown because it doesn't managed it own transaction provider.
     * <p>
     * Future: This method will send a message to SQS queue for indexing.
     *
     * @param entityManager  jpa entity manager
     * @param profileUid     profile used for creating the revision/new place
     * @param id             existing id of place for revision/null to create new
     * @param model          to copy data from, minimum data validated is done
     * @param editableFields fields that are editable
     * @throws RestrictionException unable to make changes due to account restriction
     * @throws PlaceLockedException unable to make changes due to place locking mechanism
     */
    Place publish(EntityManager entityManager, String profileUid, @Nullable String id, PlaceModel model, Set<PlaceEditableField> editableFields) throws RestrictionException, PlaceLockedException {
        Place place;
        if (id != null) {
            // Existing Revision, check locks and restriction
            checkLocks(entityManager, profileUid, id);
            RestrictionException.check(entityManager, profileUid, ProfileRestrictionType.PLACE_REVISION_WRITE);

            // If place has no change, just return existing
            place = entityManager.find(Place.class, id);
        } else {
            // Creating new
            RestrictionException.check(entityManager, profileUid, ProfileRestrictionType.PLACE_WRITE);

            // New Place if don't already exists
            place = newPlace(entityManager.find(Profile.class, profileUid));
        }

        Profile profile = entityManager.find(Profile.class, profileUid);
        PlaceRevision revision = newRevision(editableFields, profile, model, place);

        // If Revision is save as existing Place, just return
        if (id != null && PlaceModel.equals(place, revision)) {
            // Set > someone interacted with Place data
            place.setInteractedAt(new Timestamp(System.currentTimeMillis()));
            entityManager.persist(place);
            return place;
        }

        entityManager.persist(revision);
        return revision.getPlace();
    }

    /**
     * @param editableFields fields that are editable
     * @param profile        that is creating the data
     * @param model          to copy data from
     * @param place          to copy default data from if don't exist in model
     * @return newly created PlaceRevision
     */
    private PlaceRevision newRevision(Set<PlaceEditableField> editableFields, Profile profile, PlaceModel model, Place place) {
        Objects.requireNonNull(profile);

        PlaceRevision revision = JsonUtils.toObject(JsonUtils.valueToTree(place), PlaceRevision.class);
        revision.setPlace(place);
        revision.setCreatedBy(profile);

        // Only field that are editable will be edited
        for (PlaceEditableField field : editableFields) {
            field.edit(revision, model);
        }

        return revision;
    }

    /**
     * @param profile that created the Place
     * @return Place newly created with required default value set
     */
    private static Place newPlace(Profile profile) {
        Place place = new Place();
        place.setCreatedBy(profile);

        // Deep Objects
        place.setStatus(new Status.OpenStatus());
        place.setLocation(new PlaceModel.Location());

        // List Objects
        place.setSynonyms(new HashSet<>());
        place.setTags(new HashSet<>());
        place.setHours(new HashSet<>());
        return place;
    }

    /**
     * @param entityManager jpa entity manager
     * @param profileUid    profile trying to write PlaceRevision
     * @param placeId       of place to make changes
     * @throws PlaceLockedException unable to make changes due to place locking mechanism
     */
    private static void checkLocks(EntityManager entityManager, String profileUid, String placeId) throws PlaceLockedException {
        // Check if any locking is in place
        PlaceLocking locking = entityManager.find(PlaceLocking.class, placeId);

        if (locking != null) {
            // If locked, only same profile can access
            if (locking.getProfile().getUid().equals(profileUid)) {
                return;
            }

            // Admin, has rights no matter what
            if (Profile.ADMIN_ID.equals(profileUid)) {
                return;
            }

            // Else, throw locking exception
            throw new PlaceLockedException(locking.getType());
        }
    }
}
