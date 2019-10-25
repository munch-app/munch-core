package app.munch.controller;

import app.munch.exception.EditLockedException;
import app.munch.model.PlaceLocking;
import app.munch.model.Profile;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.Objects;

/**
 * @author Fuxing Loh
 * @since 2019-10-25 at 16:20
 */
@Singleton
public final class LockingController extends Controller {

    /**
     * @param entityManager jpa entity manager
     * @param profile       profile trying to write PlaceRevision
     * @param placeId       of place to make changes
     * @throws EditLockedException unable to make changes due to place locking mechanism
     * @deprecated awaiting new DataLocking entity to control locking
     */
    @Deprecated
    public void check(EntityManager entityManager, Profile profile, String placeId) throws EditLockedException {
        Objects.requireNonNull(profile);

        // Check if any locking is in place
        PlaceLocking locking = entityManager.find(PlaceLocking.class, placeId);

        if (locking != null) {
            // If locked, only same profile can access
            if (locking.getProfile().getUid().equals(profile.getUid())) {
                return;
            }

            // Admin, has rights no matter what
            if (Profile.ADMIN_ID.equals(profile.getUid())) {
                return;
            }

            // Else, throw locking exception
            throw new EditLockedException(locking.getType());
        }
    }
}
