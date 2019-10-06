package app.munch.worker;

import app.munch.model.*;
import com.instagram.InstagramApi;
import com.instagram.err.InstagramException;
import com.instagram.model.InstagramMedia;
import com.instagram.model.MediaRecentResponse;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.utils.SleepUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Fuxing Loh
 * @since 6/10/19 at 11:47 am
 */
@Singleton
public final class InstagramDataRiver {
    private static final Logger logger = LoggerFactory.getLogger(InstagramDataRiver.class);

    private final TransactionProvider provider;
    private final InstagramApi api;
    private final InstagramDataMapper dataMapper;

    @Inject
    InstagramDataRiver(TransactionProvider provider, InstagramApi api, InstagramDataMapper dataMapper) {
        this.provider = provider;
        this.api = api;
        this.dataMapper = dataMapper;
    }

    public void open(InstagramAccountConnectionTask connectionTask) throws InstagramException {
        InstagramAccountConnection connection = connectionTask.getConnection();
        Profile profile = connection.getSocial().getProfile();

        openIterator(connection.getAccessToken()).forEachRemaining(instagramMedia -> {
            provider.with(entityManager -> {
                List<ProfileMedia> list = entityManager.createQuery("FROM ProfileMedia " +
                        "WHERE type = :type AND eid = :eid", ProfileMedia.class)
                        .setParameter("type", dataMapper.mapType(instagramMedia))
                        .setParameter("eid", dataMapper.mapEid(instagramMedia))
                        .setMaxResults(1)
                        .getResultList();

                flow(entityManager, profile, instagramMedia, list.isEmpty() ? null : list.get(0));
            });
            SleepUtils.sleep(Duration.ofSeconds(1));
        });
    }

    private Iterator<InstagramMedia> openIterator(String accessToken) throws InstagramException {
        return new Iterator<>() {
            MediaRecentResponse response = api.getUserSelfMediaRecent(accessToken, 30, null);
            List<InstagramMedia> list = new ArrayList<>(response.getData());

            @Override
            public boolean hasNext() {
                if (list.isEmpty()) {
                    if (response.getPagination() != null && response.getPagination().getNextMaxId() != null) {
                        response = api.getUserSelfMediaRecent(accessToken, 30, response.getPagination().getNextMaxId());
                        list.addAll(response.getData());

                        return !list.isEmpty();
                    }
                    return false;
                }
                return true;
            }

            @Override
            public InstagramMedia next() {
                return list.remove(0);
            }
        };
    }

    private void flow(EntityManager entityManager, Profile profile, InstagramMedia instagramMedia, @Nullable ProfileMedia profileMedia) {
        if (profileMedia == null) {
            profileMedia = new ProfileMedia();
            profileMedia.setStatus(ProfileMediaStatus.PENDING);
            profileMedia.setEid(dataMapper.mapEid(instagramMedia));
            profileMedia.setType(dataMapper.mapType(instagramMedia));
            profileMedia.setCreatedAt(dataMapper.mapCreatedAt(instagramMedia));
            profileMedia.setImages(dataMapper.mapImages(profile, instagramMedia));
        }

        profileMedia.setContent(dataMapper.mapContent(instagramMedia));
        profileMedia.setMetric(dataMapper.mapMetric(instagramMedia));

        entityManager.persist(profileMedia);
    }
}
