package com.instagram;

import com.fasterxml.jackson.databind.JsonNode;
import com.instagram.err.InstagramAccessException;
import com.instagram.err.InstagramException;
import com.instagram.err.InstagramLimitException;
import com.instagram.model.AccessTokenResponse;
import com.instagram.model.MediaRecentResponse;
import com.instagram.model.UserSelfResponse;
import com.typesafe.config.ConfigFactory;
import dev.fuxing.utils.JsonUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * Date: 2/10/19
 * Time: 1:29 pm
 *
 * @author Fuxing Loh
 */
@Singleton
public final class InstagramApi {
    private static final Logger logger = LoggerFactory.getLogger(InstagramApi.class);

    private final String clientId;
    private final String clientSecret;

    @Inject
    InstagramApi() {
        if (ConfigFactory.load().hasPath("services.instagram.client-json")) {
            JsonNode json = JsonUtils.jsonToTree(ConfigFactory.load().getString("services.instagram.client-json"));
            this.clientId = json.path("clientId").asText();
            this.clientSecret = json.path("clientSecret").asText();
        } else {
            this.clientId = null;
            this.clientSecret = null;
        }
    }

    /**
     * @param accessToken user accessToken
     * @param count       max number of media to return
     * @param maxId       maxId for paginating
     * @return MediaRecentResponse
     * <pre>
     *     {
     *     "pagination":{"next_max_id":"1814626028460382812_207871973"},
     *     "data": [{
     *         "comments": {
     *             "count": 0
     *         },
     *         "caption": {
     *             "created_time": "1296710352",
     *             "text": "Inside le truc #foodtruck",
     *             "from": {
     *                 "username": "kevin",
     *                 "full_name": "Kevin Systrom",
     *                 "type": "user",
     *                 "id": "3"
     *             },
     *             "id": "26621408"
     *         },
     *         "likes": {
     *             "count": 15
     *         },
     *         "link": "http://instagr.am/p/BWrVZ/",
     *         "user": {
     *             "username": "kevin",
     *             "profile_picture": "http://distillery.s3.amazonaws.com/profiles/profile_3_75sq_1295574122.jpg",
     *             "id": "3"
     *         },
     *         "created_time": "1296710327",
     *         "images": {
     *             "low_resolution": {
     *                 "url": "http://distillery.s3.amazonaws.com/media/2011/02/02/6ea7baea55774c5e81e7e3e1f6e791a7_6.jpg",
     *                 "width": 306,
     *                 "height": 306
     *             },
     *             "thumbnail": {
     *                 "url": "http://distillery.s3.amazonaws.com/media/2011/02/02/6ea7baea55774c5e81e7e3e1f6e791a7_5.jpg",
     *                 "width": 150,
     *                 "height": 150
     *             },
     *             "standard_resolution": {
     *                 "url": "http://distillery.s3.amazonaws.com/media/2011/02/02/6ea7baea55774c5e81e7e3e1f6e791a7_7.jpg",
     *                 "width": 612,
     *                 "height": 612
     *             }
     *         },
     *         "type": "image",
     *         "users_in_photo": [],
     *         "filter": "Earlybird",
     *         "tags": ["foodtruck"],
     *         "id": "22721881",
     *         "location": {
     *             "latitude": 37.778720183610183,
     *             "longitude": -122.3962783813477,
     *             "id": "520640",
     *             "street_address": "",
     *             "name": "Le Truc"
     *         }
     *     },
     *     {
     *         "videos": {
     *             "low_resolution": {
     *                 "url": "http://distilleryvesper9-13.ak.instagram.com/090d06dad9cd11e2aa0912313817975d_102.mp4",
     *                 "width": 480,
     *                 "height": 480
     *             },
     *             "standard_resolution": {
     *                 "url": "http://distilleryvesper9-13.ak.instagram.com/090d06dad9cd11e2aa0912313817975d_101.mp4",
     *                 "width": 640,
     *                 "height": 640
     *             },
     *         "comments": {
     *             "count": 2
     *         },
     *         "caption": null,
     *         "likes": {
     *             "count": 1
     *         },
     *         "link": "http://instagr.am/p/D/",
     *         "created_time": "1279340983",
     *         "images": {
     *             "low_resolution": {
     *                 "url": "http://distilleryimage2.ak.instagram.com/11f75f1cd9cc11e2a0fd22000aa8039a_6.jpg",
     *                 "width": 306,
     *                 "height": 306
     *             },
     *             "thumbnail": {
     *                 "url": "http://distilleryimage2.ak.instagram.com/11f75f1cd9cc11e2a0fd22000aa8039a_5.jpg",
     *                 "width": 150,
     *                 "height": 150
     *             },
     *             "standard_resolution": {
     *                 "url": "http://distilleryimage2.ak.instagram.com/11f75f1cd9cc11e2a0fd22000aa8039a_7.jpg",
     *                 "width": 612,
     *                 "height": 612
     *             }
     *         },
     *         "type": "video",
     *         "users_in_photo": null,
     *         "filter": "Vesper",
     *         "tags": [],
     *         "id": "363839373298",
     *         "user": {
     *             "username": "kevin",
     *             "full_name": "Kevin S",
     *             "profile_picture": "http://distillery.s3.amazonaws.com/profiles/profile_3_75sq_1295574122.jpg",
     *             "id": "3"
     *         },
     *         "location": null
     *     },
     *    ]
     * }
     * </pre>
     */
    public MediaRecentResponse getUserSelfMediaRecent(String accessToken, int count, @Nullable String maxId) throws URISyntaxException, IOException {
        URIBuilder builder = new URIBuilder("https://api.instagram.com/v1/users/self/media/recent/");
        builder.addParameter("access_token", accessToken);
        builder.addParameter("count", String.valueOf(count));

        if (maxId != null) {
            builder.addParameter("max_id", maxId);
        }

        HttpResponse response = Request.Get(builder.build())
                .execute()
                .returnResponse();

        return parseResponse(response, MediaRecentResponse.class);
    }

    /**
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
     *
     * @param accessToken user accessToken
     * @return UserSelfResponse, see pre
     * @throws URISyntaxException url building error
     * @throws IOException        network related error
     */
    public UserSelfResponse getUserSelf(String accessToken) throws URISyntaxException, IOException {
        URIBuilder builder = new URIBuilder("https://api.instagram.com/v1/users/self/");
        builder.addParameter("access_token", accessToken);

        HttpResponse response = Request.Get(builder.build())
                .execute()
                .returnResponse();

        String json = EntityUtils.toString(response.getEntity(), "UTF-8");

        return JsonUtils.toObject(json, UserSelfResponse.class);
    }

    /**
     * <pre>
     * {
     *     "access_token": "fb2e77d.47a0479900504cb3ab4a1f626d174d2d",
     *     "user": {
     *         "id": "1574083",
     *         "username": "snoopdogg",
     *         "full_name": "Snoop Dogg",
     *         "profile_picture": "..."
     *     }
     * }
     * </pre>
     *
     * @param code        the exact code you received during the authorization step.
     * @param redirectUri the redirect_uri you used in the authorization request.
     * @return AccessTokenResponse, see pre
     * @throws IOException network related error
     */
    public AccessTokenResponse postAccessToken(String code, String redirectUri) throws IOException {
        Objects.requireNonNull(clientId, "services.instagram.client-json.clientId required");
        Objects.requireNonNull(clientSecret, "services.instagram.client-json.clientSecret required");

        HttpResponse response = Request.Post("https://api.instagram.com/oauth/access_token")
                .bodyForm(
                        new BasicNameValuePair("client_id", clientId),
                        new BasicNameValuePair("client_secret", clientSecret),
                        new BasicNameValuePair("grant_type", "authorization_code"),
                        new BasicNameValuePair("code", code),
                        new BasicNameValuePair("redirect_uri", redirectUri)
                )
                .execute()
                .returnResponse();

        return parseResponse(response, AccessTokenResponse.class);
    }

    private static <T> T parseResponse(HttpResponse response, Class<T> clazz) throws IOException {
        String json = EntityUtils.toString(response.getEntity(), "UTF-8");
        int status = response.getStatusLine().getStatusCode();

        if (status == 200) {
            return JsonUtils.toObject(json, clazz);
        }

        JsonNode node = JsonUtils.jsonToTree(json);
        String type = node.path("meta").path("error_type").asText();
        String message = node.path("meta").path("error_message").asText();
        int code = node.path("meta").path("code").asInt(500);

        switch (type) {
            case "OAuthRateLimitException":
                throw new InstagramLimitException(message);

            case "OAuthAccessTokenException":
                throw new InstagramAccessException(message);

            default:
                throw new InstagramException(code, type + ": " + message);
        }
    }

}
