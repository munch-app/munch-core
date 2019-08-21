package app.munch.api.v22v23;

import app.munch.image.ImageEntityManager;
import app.munch.model.ImageSource;
import app.munch.model.Profile;
import app.munch.model.Tag;
import app.munch.model.TagType;
import dev.fuxing.err.ForbiddenException;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.transport.service.TransportResult;
import dev.fuxing.transport.service.TransportService;
import munch.data.client.TagClient;
import munch.file.Image;
import munch.instagram.data.InstagramAccount;
import munch.instagram.data.InstagramAccountClient;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 19/8/19
 * Time: 4:42 am
 */
@Singleton
public final class MigrationService implements TransportService {

    private final TransactionProvider provider;
    private final TagClient tagClient;
    private final InstagramAccountClient accountClient;
    private final ImageEntityManager imageEntityManager;

    @Inject
    MigrationService(TransactionProvider provider, TagClient tagClient, InstagramAccountClient accountClient, ImageEntityManager imageEntityManager) {
        this.provider = provider;
        this.tagClient = tagClient;
        this.accountClient = accountClient;
        this.imageEntityManager = imageEntityManager;
    }

    @Override
    public void route() {
        PATH("/v22-v23/migrations", () -> {
            POST("/tags", this::tags);
            POST("/partners", this::partners);
            POST("/admin", this::admin);
            POST("/creators", this::creators);
        });
    }

    public TransportResult creators(TransportContext ctx) {
        boolean persist = ctx.queryBool("persist");

        provider.with(entityManager -> {


            if (persist) {
                // TODO(fuxing):
            }
        });
        return TransportResult.ok();
    }

    public TransportResult admin(TransportContext ctx) {
        provider.with(entityManager -> {
            Profile profile = entityManager.find(Profile.class, Profile.ADMIN_ID);
            if (profile != null) return;

            Profile admin = new Profile();
            admin.setUid(Profile.ADMIN_ID);
            admin.setName("Admin");
            admin.setUsername("madmin");
            entityManager.persist(admin);

            Profile compat = new Profile();
            compat.setUid(Profile.COMPAT_ID);
            compat.setUsername("mcompat");
            compat.setName("Compact");
            entityManager.persist(compat);
        });
        return TransportResult.ok();
    }

    public TransportResult tags(TransportContext ctx) {
        provider.with(entityManager -> {
            List list = entityManager.createQuery("FROM Tag")
                    .setMaxResults(1)
                    .getResultList();

            if (!list.isEmpty()) {
                throw new ForbiddenException();
            }

            tagClient.iterator().forEachRemaining(deprecatedTag -> {
                Tag tag = new Tag();
                tag.setType(parse(deprecatedTag.getType().name()));
                tag.setName(deprecatedTag.getName());
                entityManager.persist(tag);
            });
        });
        return TransportResult.ok();
    }

    public TransportResult partners(TransportContext ctx) {
        accountClient.iterator(30).forEachRemaining(account -> {
            InstagramAccount.@NotNull @Valid User user = account.getUser();
            if (StringUtils.isBlank(account.getUser().getFullName())) return;

            @NotBlank String username = user.getUsername().toLowerCase().replaceAll("[^a-z0-9]", "");
            provider.with(entityManager -> {
                List list = entityManager.createQuery("FROM Profile " +
                        "WHERE username = :username")
                        .setParameter("username", username)
                        .getResultList();

                if (!list.isEmpty()) return;

                Profile profile = new Profile();
                profile.setUsername(username);
                profile.setName(user.getFullName());
                profile.setBio(user.getBio());

                entityManager.persist(profile);

                Image image = user.getProfileImage();
                if (image != null) {
                    image.getSizes().stream()
                            .max(Comparator.comparingInt(Image.Size::getHeight))
                            .map(Image.Size::getUrl)
                            .ifPresent(url -> {
                                try {
                                    profile.setImage(imageEntityManager.post(entityManager, ImageSource.PROFILE, profile, url));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                }

            });
        });
        return TransportResult.ok();
    }

    private TagType parse(String type) {
        switch (type) {
            case "Food":
                return TagType.FOOD;
            case "Cuisine":
                return TagType.CUISINE;
            case "Amenities":
            case "Establishment":
            case "Timing":
            case "Requirement":
            default:
                return TagType.AMENITIES;
        }
    }
}
