package com.instagram;

import com.fasterxml.jackson.databind.JsonNode;
import com.instagram.model.AccessTokenResponse;
import com.instagram.model.InstagramUser;
import com.typesafe.config.ConfigFactory;
import dev.fuxing.utils.JsonUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.URISyntaxException;

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
        String json = ConfigFactory.load().getString("services.instagram.client-json");
        JsonNode node = JsonUtils.jsonToTree(json);
        this.clientId = node.path("clientId").asText();
        this.clientSecret = node.path("clientSecret").asText();
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
     * @return InstagramUser, see pre
     * @throws URISyntaxException url building error
     * @throws IOException        network related error
     */
    public InstagramUser getUserSelf(String accessToken) throws URISyntaxException, IOException {
        URIBuilder builder = new URIBuilder("https://api.instagram.com/v1/users/self/");
        builder.addParameter("access_token", accessToken);

        HttpResponse response = Request.Get(builder.build())
                .execute()
                .returnResponse();

        String json = EntityUtils.toString(response.getEntity(), "UTF-8");
        return JsonUtils.toObject(json, InstagramUser.class);
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

        String json = EntityUtils.toString(response.getEntity(), "UTF-8");
        int status = response.getStatusLine().getStatusCode();

        if (status == 200) {
            return JsonUtils.toObject(json, AccessTokenResponse.class);
        }

        logger.error("Instagram Authentication Failed: {}", json);
        throw new RuntimeException("Instagram authentication failed.");
    }

}
