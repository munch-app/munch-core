package munch.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import munch.restful.core.exception.CodeException;
import munch.restful.server.jwt.AuthenticatedToken;
import munch.restful.server.jwt.TokenAuthenticator;
import munch.user.client.UserProfileClient;
import spark.Request;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 2019-03-15
 * Time: 22:47
 * Project: munch-core
 */
@Singleton
public final class ApiAuthenticator {

    private final TokenAuthenticator authenticator;
    private final UserProfileClient profileClient;

    @Inject
    public ApiAuthenticator(TokenAuthenticator authenticator, UserProfileClient profileClient) {
        this.authenticator = authenticator;
        this.profileClient = profileClient;
    }

    public ApiRequest authenticate(Request request) {
        DecodedJWT jwt = getJWT(request);
        if (jwt == null) {
            return new ApiRequest(request, null, null);
        }

        AuthenticatedToken token = authenticator.authenticate(jwt);
        return new ApiRequest(request, token, () -> profileClient.get(token.getSubject()));
    }

    @Nullable
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
