package app.munch.worker;

import app.munch.model.ProfileMedia;
import app.munch.model.ProfileMediaStatus;
import app.munch.model.ProfileSocial;
import munch.instagram.data.InstagramMedia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;

/**
 * @author Fuxing Loh
 * @since 6/10/19 at 11:47 am
 */
@Singleton
public final class InstagramDataRiver {
    private static final Logger logger = LoggerFactory.getLogger(InstagramDataRiver.class);

    private final InstagramDataMapper dataMapper;

    @Inject
    InstagramDataRiver(InstagramDataMapper dataMapper) {
        this.dataMapper = dataMapper;
    }

    public void flow(EntityManager entityManager, ProfileSocial social, InstagramMedia instagramMedia, @Nullable ProfileMedia profileMedia) {
        if (profileMedia == null) {
            profileMedia = new ProfileMedia();
            profileMedia.setStatus(ProfileMediaStatus.PENDING);
            profileMedia.setEid(dataMapper.mapEid(instagramMedia));
            profileMedia.setType(dataMapper.mapType(instagramMedia));
            profileMedia.setCreatedAt(dataMapper.mapCreatedAt(instagramMedia));
            profileMedia.setImages(dataMapper.mapImages(social.getProfile(), instagramMedia));

            profileMedia.setSocial(social);
            profileMedia.setProfile(social.getProfile());
        }

        profileMedia.setContent(dataMapper.mapContent(instagramMedia));
        profileMedia.setMetric(dataMapper.mapMetric(instagramMedia));

        entityManager.persist(profileMedia);
    }
}
