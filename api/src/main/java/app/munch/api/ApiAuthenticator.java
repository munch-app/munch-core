package app.munch.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.fuxing.transport.service.TransportContext;
import munch.restful.core.exception.AuthenticationException;
import munch.restful.core.exception.CodeException;
import munch.restful.server.jwt.AuthenticatedToken;
import munch.restful.server.jwt.TokenAuthenticator;
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
    /*
        This class is using components of Restful Server that is getting deprecating.
        Move the TokenAuthenticator code in house whenever possible.
     */

    private final TokenAuthenticator authenticator;

    @Inject
    public ApiAuthenticator(TokenAuthenticator authenticator) {
        this.authenticator = authenticator;
    }

    public ApiRequest authenticate(TransportContext ctx) throws AuthenticationException {
        DecodedJWT jwt = getJWT(ctx.request());
        if (jwt == null) {
            return ApiRequest.of(ctx, null);
        }

        AuthenticatedToken token = authenticator.authenticate(jwt);
        return ApiRequest.of(ctx, token);
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
