package app.munch.instagram;

import app.munch.instagram.err.InstagramAccessException;
import app.munch.instagram.err.InstagramException;
import app.munch.instagram.err.InstagramLimitException;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.err.ExceptionParser;
import dev.fuxing.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;

import javax.annotation.Nullable;
import javax.inject.Singleton;

/**
 * Date: 2/10/19
 * Time: 9:57 am
 *
 * @author Fuxing Loh
 */
@Singleton
public final class InstagramApi {

    /**
     * @param accessToken user accessToken
     * @param count       max number of media to return
     * @param maxId       maxId for paginating
     * @return JsonNode
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
    public JsonNode getUserSelfMediaRecent(String accessToken, int count, @Nullable String maxId) {
        try {
            URIBuilder builder = new URIBuilder("https://api.instagram.com/v1/users/self/media/recent/");
            builder.addParameter("access_token", accessToken);
            builder.addParameter("count", String.valueOf(count));

            if (maxId != null) {
                builder.addParameter("max_id", maxId);
            }

            HttpResponse response = Request.Get(builder.build())
                    .execute()
                    .returnResponse();

            String json = EntityUtils.toString(response.getEntity(), "UTF-8");
            JsonNode node = JsonUtils.jsonToTree(json);
            validateNode(node);
            return node;
        } catch (Exception e) {
            ExceptionParser.parse(e);
            throw new RuntimeException(e);
        }
    }

    private static void validateNode(JsonNode node) throws InstagramException {
        String type = node.path("meta").path("error_type").asText();
        String message = node.path("meta").path("error_message").asText();
        int code = node.path("meta").path("code").asInt(500);

        if (StringUtils.isNotBlank(type)) {
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
}
