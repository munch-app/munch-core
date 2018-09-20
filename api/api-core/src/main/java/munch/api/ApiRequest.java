package munch.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import munch.restful.core.exception.AuthenticationException;
import munch.restful.core.exception.CodeException;
import munch.restful.server.jwt.AuthenticatedToken;
import munch.restful.server.jwt.TokenAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Created by: Fuxing
 * Date: 17/9/18
 * Time: 4:06 PM
 * Project: munch-core
 */
public final class ApiRequest {
    private static final Logger logger = LoggerFactory.getLogger(ApiRequest.class);
    public static final String HEADER_USER_LAT_LNG = "User-Lat-Lng";
    public static final String HEADER_USER_LOCAL_TIME = "User-Local-Time";

    private String userId;
    private DecodedJWT jwt;
    private AuthenticatedToken token;

    private String latLng;
    private LocalDateTime localTime;

    ApiRequest(Request request, TokenAuthenticator authenticator) {
        this.jwt = getJWT(request);
        if (this.jwt != null) {
            this.token = authenticator.authenticate(jwt);
            this.userId = this.token.getSubject();
        }

        latLng = request.headers(HEADER_USER_LAT_LNG);
        String timeString = request.headers(HEADER_USER_LOCAL_TIME);
        if (timeString != null) localTime = LocalDateTime.parse(timeString);
    }

    /**
     * @return whether user is authenticated
     */
    public boolean isAuthenticated() {
        return token != null;
    }

    /**
     * @return decoded JWT
     */
    @Nullable
    public DecodedJWT getJWT() {
        return jwt;
    }

    /**
     * @return get authenticated userId
     */
    public String getUserId() {
        if (userId == null) throw new AuthenticationException(403, "Forbidden");
        return userId;
    }

    /**
     * @return user latLng
     */
    @Nullable
    public String getLatLng() {
        return latLng;
    }

    /**
     * @return user local time
     */
    @Nullable
    public LocalDateTime getLocalTime() {
        return localTime;
    }


    /**
     * @return Optional String UserId if exist
     */
    public Optional<String> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    /**
     * LatLng is not validated but returned if exists
     * If exist, it should be formatted as such lat,lng
     * e.g. "1.34,103.8"
     *
     * @return Optional String of User Latitude Longitude if exists
     */
    public Optional<String> optionalLatLng() {
        return Optional.ofNullable(latLng);
    }

    /**
     * LocalDateTime is validated and returned if exists
     * Using Format: ISO_LOCAL_DATE_TIME
     * Example: '2011-12-03T10:15:30'
     *
     * @return Optional String of User Local Time if exists
     */
    public Optional<LocalDateTime> optionalLocalTime() {
        return Optional.ofNullable(localTime);
    }

    private static DecodedJWT getJWT(Request request) {
        String token = getJWTToken(request);
        if (token == null) return null;

        try {
            return JWT.decode(token);
        } catch (JWTDecodeException exception) {
            // Invalid token
            throw new CodeException(403);
        }
    }

    @Nullable
    private static String getJWTToken(Request request) {
        final String value = request.headers("Authorization");
        if (value == null || !value.toLowerCase().startsWith("bearer")) {
            return null;
        }

        String[] parts = value.split(" ");
        if (parts.length < 2) {
            return null;
        }

        return parts[1].trim();
    }
}
