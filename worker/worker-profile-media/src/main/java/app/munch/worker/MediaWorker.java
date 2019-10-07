package app.munch.worker;

import app.munch.database.DatabaseModule;
import app.munch.model.*;
import catalyst.edit.PlaceEdit;
import catalyst.edit.PlaceEditClient;
import catalyst.link.PlaceLink;
import catalyst.link.PlaceLinkClient;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.utils.SleepUtils;
import munch.instagram.data.InstagramAccount;
import munch.instagram.data.InstagramAccountClient;
import munch.instagram.data.InstagramMedia;
import munch.instagram.data.InstagramMediaClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * @author Fuxing Loh
 * @since 7/10/19 at 5:00 pm
 */
@Singleton
public final class MediaWorker implements WorkerRunner {
    private static final Logger logger = LoggerFactory.getLogger(MediaWorker.class);

    private final TransactionProvider provider;
    private final InstagramAccountClient accountClient;
    private final InstagramMediaClient mediaClient;

    private final InstagramDataRiver dataRiver;

    private final PlaceEditClient editClient;
    private final PlaceLinkClient linkClient;

    @Inject
    MediaWorker(TransactionProvider provider, InstagramAccountClient accountClient, InstagramMediaClient mediaClient, InstagramDataRiver dataRiver, PlaceEditClient editClient, PlaceLinkClient linkClient) {
        this.provider = provider;
        this.accountClient = accountClient;
        this.mediaClient = mediaClient;
        this.dataRiver = dataRiver;
        this.editClient = editClient;
        this.linkClient = linkClient;
    }

    @Override
    public String groupUid() {
        return null;
    }

    @Override
    public void run(WorkerTask task) {
        accountClient.iterator(20).forEachRemaining(account -> {
            createAccount(account);
            SleepUtils.sleep(Duration.ofSeconds(1));
        });

        mediaClient.iterator().forEachRemaining(instagramMedia -> {
            createMedia(instagramMedia);
            SleepUtils.sleep(Duration.ofSeconds(1));
        });

        editClient.iterator("media.instagram.com", 30).forEachRemaining(edit -> {
            linkClient.iteratorSourceId(edit.getSource(), edit.getId(), 30).forEachRemaining(placeLink -> {
                createMention(edit, placeLink);
            });
            SleepUtils.sleep(Duration.ofSeconds(1));
        });
    }

    private void createAccount(InstagramAccount account) {
        @NotBlank String eid = account.getAccountId();
        @NotBlank String username = account.getUser().getUsername();
        String accessToken = account.getAccessToken();

        provider.with(entityManager -> {
            List<ProfileSocial> list = entityManager.createQuery("FROM ProfileSocial " +
                    "WHERE type = :type AND eid = :eid", ProfileSocial.class)
                    .setParameter("type", ProfileSocialType.INSTAGRAM)
                    .setParameter("eid", eid)
                    .getResultList();

            if (list.isEmpty()) {
                Profile profile = Profile.findByUsername(entityManager, username);
                if (profile == null) {
                    profile = new Profile();
                    profile.setUsername(username);
                    profile.setName(StringUtils.defaultString(account.getUser().getFullName(), username));
                    entityManager.persist(profile);
                }

                ProfileSocial social = new ProfileSocial();
                social.setEid(eid);
                social.setType(ProfileSocialType.INSTAGRAM);
                social.setProfile(profile);
                social.setName(account.getUser().getUsername());
                social.setStatus(ProfileSocialStatus.CONNECTED);
                entityManager.persist(social); // Persisted

                InstagramAccountConnection connection = new InstagramAccountConnection();
                connection.setStatus(InstagramAccountConnectionStatus.DISCONNECTED);
                connection.setAccessToken(accessToken);
                connection.setSocial(social);
                entityManager.persist(connection); // Persisted
            }
        });
    }

    private void createMedia(InstagramMedia media) {
        String username = media.getUser().getUsername();

        provider.with(entityManager -> {
            Profile profile = Profile.findByUsername(entityManager, username);
            ProfileSocial social = entityManager.createQuery("FROM ProfileSocial " +
                    "WHERE profile = :profile", ProfileSocial.class)
                    .setParameter("profile", profile)
                    .getSingleResult();

            List<ProfileMedia> list = entityManager.createQuery("FROM ProfileMedia " +
                    "WHERE type = :type AND eid = :eid", ProfileMedia.class)
                    .setParameter("type", ProfileSocialType.INSTAGRAM)
                    .setParameter("eid", media.getMediaId())
                    .setMaxResults(1)
                    .getResultList();

            dataRiver.flow(entityManager, social, media, list.isEmpty() ? null : list.get(0));
        });
    }

    private void createMention(PlaceEdit edit, PlaceLink link) {
        if (link.getPlaceId().equals(PlaceLink.BLOCKED_PLACE_ID)) {
            return;
        }

        String cid = link.getPlaceId();

        provider.with(entityManager -> Optional.of(entityManager.createQuery("FROM Place WHERE cid = :cid", Place.class)
                .setParameter("cid", cid)
                .getResultList())
                .filter(places -> !places.isEmpty())
                .map(places -> places.get(0))
                .ifPresent(place -> {
                    ProfileMedia media = entityManager.createQuery("FROM ProfileMedia WHERE eid = :eid", ProfileMedia.class).getSingleResult();
                    media.setStatus(ProfileMediaStatus.PUBLIC);
                    entityManager.persist(media);

                    List<Mention> list = entityManager.createQuery("FROM Mention WHERE media.id = :id ", Mention.class)
                            .setParameter("id", media.getId())
                            .getResultList();

                    if (list.isEmpty()) {
                        Mention mention = new Mention();
                        mention.setStatus(MentionStatus.PUBLIC);
                        mention.setType(MentionType.MEDIA);
                        mention.setMedia(media);
                        mention.setProfile(media.getProfile());
                        mention.setPlace(place);


                        mention.setCreatedBy(entityManager.find(Profile.class, Profile.COMPAT_ID));
                    }
                }));
    }

    public static void main(String[] args) {
        WorkerRunner.start(MediaWorker.class,
                new DatabaseModule()
        );
    }
}
