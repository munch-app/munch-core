package app.munch.api.social;

import app.munch.model.ProfileSocial;
import app.munch.model.ProfileSocialType;
import dev.fuxing.transport.service.TransportService;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import java.util.List;

/**
 * Date: 2/10/19
 * Time: 1:37 pm
 *
 * @author Fuxing Loh
 */
public interface SocialService extends TransportService {

    @Nullable
    default ProfileSocial findByTypeEid(EntityManager entityManager, ProfileSocialType type, String eid) {
        List<ProfileSocial> list = entityManager.createQuery("FROM ProfileSocial " +
                "WHERE type = :type AND eid = :eid", ProfileSocial.class)
                .setParameter("type", type)
                .setParameter("eid", eid)
                .getResultList();

        if (list.isEmpty()) return null;

        return list.get(0);
    }
}
