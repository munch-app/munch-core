package app.munch.controller;

import app.munch.model.Profile;
import app.munch.model.ProfileSocial;
import app.munch.model.ProfileSocialStatus;
import app.munch.model.ProfileSocialType;
import app.munch.model.group.ProfileSocialAuthenticateGroup;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.err.ConflictException;
import dev.fuxing.err.UnknownException;
import dev.fuxing.err.ValidationException;
import dev.fuxing.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.List;
import java.util.function.Function;

/**
 * Date: 1/10/19
 * Time: 3:00 pm
 *
 * @author Fuxing Loh
 */
@Singleton
public final class ProfileSocialController extends Controller {

    public void authenticate(ProfileSocial social, Function<EntityManager, Profile> mapper) {
        ValidationException.validate(social, ProfileSocialAuthenticateGroup.class);
        final ProfileSocial mapped = mapProfileSocial(social);

        provider.with(entityManager -> {
            Profile profile = mapper.apply(entityManager);
            if (profile == null) throw new ConflictException("Profile not found.");

            ProfileSocial entity = getEntity(entityManager, mapped.getType(), mapped.getEid());
            if (entity != null) {
                if (!entity.getProfile().getUid().equals(profile.getUid())) {
                    throw new ConflictException("This account is owned by another profile.");
                }
            } else {
                entity = new ProfileSocial();
                entity.setEid(mapped.getEid());
                entity.setType(mapped.getType());
                entity.setProfile(profile);
            }

            entity.setName(mapped.getName());
            entity.setStatus(ProfileSocialStatus.CONNECTED);
            entity.setConnectedAt(new Timestamp(System.currentTimeMillis()));
            entityManager.persist(entity);
        });
    }

    private ProfileSocial getEntity(EntityManager entityManager, ProfileSocialType type, String eid) {
        List<ProfileSocial> list = entityManager.createQuery("FROM ProfileSocial " +
                "WHERE type = :type AND eid = :eid", ProfileSocial.class)
                .setParameter("type", type)
                .setParameter("eid", eid)
                .getResultList();

        if (list.isEmpty()) return null;

        return list.get(0);
    }

    private ProfileSocial mapProfileSocial(ProfileSocial social) {
        switch (social.getType()) {
            case INSTAGRAM:
                return mapInstagram(social);

            default:
                throw new ValidationException("type", "is not supported");
        }
    }

    private ProfileSocial mapInstagram(ProfileSocial social) {
        ProfileSocial.Secrets secrets = social.getSecrets();
        String accessToken = secrets.getAccessToken();
        if (accessToken == null) throw new ValidationException("accessToken", "required");

        try {
            JsonNode user = Instagram.getUserSelf(accessToken);
            social.setEid(user.path("id").asText());
            social.setName(StringUtils.defaultString(user.path("username").asText(), "Instagram"));
            return social;
        } catch (URISyntaxException | IOException e) {
            throw new UnknownException(e);
        }
    }

    private static final class Instagram {

        /**
         * @param accessToken user accessToken
         * @return JsonNode
         * <pre>
         * {
         *   "data": {
         *     "id": "1574083",
         *     "username": "snoopdogg",
         *     "full_name": "Snoop Dogg",
         *     "profile_picture": "http://distillery.s3.amazonaws.com/profiles/profile_1574083_75sq_1295469061.jpg",
         *     "bio": "This is my bio",
         *     "website": "http://snoopdogg.com",
         *     "is_business": false,
         *     "counts": {
         *       "media": 1320,
         *       "follows": 420,
         *       "followed_by": 3410
         *     }
         *   }
         * }
         * </pre>
         */
        public static JsonNode getUserSelf(String accessToken) throws URISyntaxException, IOException {
            URIBuilder builder = new URIBuilder("https://api.instagram.com/v1/users/self/");
            builder.addParameter("access_token", accessToken);

            HttpResponse response = Request.Get(builder.build())
                    .execute()
                    .returnResponse();

            String json = EntityUtils.toString(response.getEntity(), "UTF-8");
            return JsonUtils.jsonToTree(json);
        }

    }
}
