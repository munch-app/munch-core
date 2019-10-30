package app.munch.controller;

import app.munch.exception.RestrictionException;
import app.munch.model.Profile;
import app.munch.model.ProfileRestrictionType;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;

/**
 * @author Fuxing Loh
 * @since 2019-10-25 at 16:26
 */
@Singleton
public final class RestrictionController extends AbstractController {

    public void check(EntityManager entityManager, Profile profile, ProfileRestrictionType type) throws RestrictionException {
        Objects.requireNonNull(profile);

        // Check ProfileRestriction
        List list = entityManager.createQuery("FROM ProfileRestriction " +
                "WHERE profile = :profile AND type = :rType")
                .setParameter("profile", profile)
                .setParameter("rType", type)
                .getResultList();

        if (list.isEmpty()) return;
        throw new RestrictionException(type);
    }

}
