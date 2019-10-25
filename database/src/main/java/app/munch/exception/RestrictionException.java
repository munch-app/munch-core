package app.munch.exception;

import app.munch.model.Profile;
import app.munch.model.ProfileRestrictionType;
import dev.fuxing.err.ExceptionParser;
import dev.fuxing.err.TransportException;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 2/9/19
 * Time: 12:05 pm
 */
public final class RestrictionException extends TransportException {

    static {
        ExceptionParser.register(RestrictionException.class, RestrictionException::new);
    }

    RestrictionException(TransportException e) {
        super(e);
    }

    public RestrictionException(ProfileRestrictionType type) {
        super(403, RestrictionException.class, "You are restricted from access this resource: '" + type.name() + "'");
    }

    @Deprecated
    public static void check(EntityManager entityManager, Profile profile, ProfileRestrictionType type) {
        Objects.requireNonNull(profile);

        check(entityManager, profile.getUid(), type);
    }

    @Deprecated
    public static void check(EntityManager entityManager, String profileUid, ProfileRestrictionType type) throws RestrictionException {
        Objects.requireNonNull(profileUid);

        // Check ProfileRestriction
        List list = entityManager.createQuery("FROM ProfileRestriction " +
                "WHERE profile.uid = :profileUid AND type = :rType")
                .setParameter("profileUid", profileUid)
                .setParameter("rType", type)
                .getResultList();

        if (list.isEmpty()) return;
        throw new RestrictionException(type);
    }
}
