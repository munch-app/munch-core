package app.munch.controller;

import app.munch.elastic.ElasticIndexPublisher;
import app.munch.exception.EditLockedException;
import app.munch.exception.RestrictionException;
import app.munch.model.*;
import dev.fuxing.err.ConflictException;
import dev.fuxing.utils.JsonUtils;

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
public class PlaceController extends Controller {

    private final LockingController locking;
    private final RestrictionController restriction;
    private final ElasticIndexPublisher documentQueue;

    @Inject
    public PlaceController(LockingController locking, RestrictionController restriction, ElasticIndexPublisher documentQueue) {
        this.locking = locking;
        this.restriction = restriction;
        this.documentQueue = documentQueue;
    }

    /**
     * @param model           to create the place data
     * @param editableFields  fields that are editable
     * @param profileProvider profile used for creating the place
     * @return Created Place
     * @throws RestrictionException unable to make changes due to account restriction
     */
    public Place create(PlaceModel model, Set<PlaceEditableField> editableFields, Function<EntityManager, Profile> profileProvider) throws RestrictionException {
        return provider.reduce(entityManager -> {
            Profile profile = profileProvider.apply(entityManager);
            if (profile == null) {
                throw new ConflictException("Profile not available.");
            }
            return create(entityManager, profile, model, editableFields);
        });
    }

    /**
     * Revision will only be created if there is a change in data
     *
     * @param id              of the existing place to mutate
     * @param model           to write/mutate into existing place
     * @param editableFields  fields that are editable
     * @param profileProvider profile used for creating the revision
     * @return Mutated Place, (Will not mutate if no changes)
     * @throws RestrictionException unable to make changes due to account restriction
     * @throws EditLockedException  unable to make changes due to place locking mechanism
     */
    public Place update(String id, PlaceModel model, Set<PlaceEditableField> editableFields, Function<EntityManager, Profile> profileProvider) throws RestrictionException, EditLockedException {
        Objects.requireNonNull(id);
        Objects.requireNonNull(model);

        return provider.reduce(entityManager -> {
            Profile profile = profileProvider.apply(entityManager);
            if (profile == null) {
                throw new ConflictException("Profile not available.");
            }

            return update(entityManager, profile, id, model, editableFields);
        });
    }

    /**
     * @param entityManager  jpa entity manager
     * @param profile        profile used for creating the revision/new place
     * @param model          to copy data from, minimum data validated is done
     * @param editableFields fields that are editable
     * @return created Place
     */
    Place create(EntityManager entityManager, Profile profile, PlaceModel model, Set<PlaceEditableField> editableFields) {
        restriction.check(entityManager, profile, ProfileRestrictionType.PLACE_WRITE);

        Place place = newPlace(profile);
        PlaceRevision revision = newRevision(editableFields, profile, model, place);

        entityManager.persist(revision);
        documentQueue.queue(place);
        return revision.getPlace();
    }

    /**
     * Revision will only be created if there is a change in data
     *
     * @param entityManager  jpa entity manager
     * @param profile        profile used for creating the revision/new place
     * @param id             existing id of place for revision/null to create new
     * @param model          to copy data from, minimum data validated is done
     * @param editableFields fields that are editable
     * @return updated Place
     */
    Place update(EntityManager entityManager, Profile profile, String id, PlaceModel model, Set<PlaceEditableField> editableFields) {
        restriction.check(entityManager, profile, ProfileRestrictionType.PLACE_REVISION_WRITE);
        locking.check(entityManager, profile, id);

        Place place = entityManager.find(Place.class, id);
        PlaceRevision revision = newRevision(editableFields, profile, model, place);

        // If Revision is save as existing Place, just return
        if (PlaceModel.equals(place, revision)) {
            // Set when someone interacted with Place data
            place.setInteractedAt(new Timestamp(System.currentTimeMillis()));
            entityManager.persist(place);
            return place;
        }

        entityManager.persist(revision);
        documentQueue.queue(place);
        return revision.getPlace();
    }

    /**
     * @param editableFields fields that are editable
     * @param profile        that is creating the data
     * @param model          to copy data from
     * @param place          to copy default data from if don't exist in model
     * @return newly created PlaceRevision
     */
    private static PlaceRevision newRevision(Set<PlaceEditableField> editableFields, Profile profile, PlaceModel model, Place place) {
        Objects.requireNonNull(profile);
        Objects.requireNonNull(model);
        Objects.requireNonNull(place);

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
        Objects.requireNonNull(profile);

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
}
