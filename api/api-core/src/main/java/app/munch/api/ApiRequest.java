package app.munch.api;

import dev.fuxing.err.ForbiddenException;
import dev.fuxing.transport.service.TransportContext;
import munch.restful.server.jwt.AuthenticatedToken;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

/**
 * Created by: Fuxing
 * Date: 17/9/18
 * Time: 4:06 PM
 * Project: munch-core
 */
public final class ApiRequest {
    public static final String HEADER_LOCAL_LAT_LNG = "Local-Lat-Lng";
    public static final String HEADER_LOCAL_ZONE_ID = "Local-Zone-Id";

    private final String accountId;
    private final String latLng;
    private final ZoneId zoneId;

    private final AuthenticatedToken token;

    private ApiRequest(String accountId, String latLng, ZoneId zoneId, AuthenticatedToken token) {
        this.accountId = accountId;
        this.latLng = latLng;
        this.zoneId = zoneId;
        this.token = token;
    }

    public static ApiRequest of(TransportContext ctx, @Nullable AuthenticatedToken token) {
        String latLng = ctx.getHeader(HEADER_LOCAL_LAT_LNG);
        String userId = token != null ? token.getSubject() : null;
        String rawZoneId = ctx.getHeader(HEADER_LOCAL_ZONE_ID);
        ZoneId zoneId = rawZoneId != null ? ZoneId.of(rawZoneId) : ZoneId.of("Asia/Singapore");

        return new ApiRequest(userId, latLng, zoneId, token);
    }

    /**
     * @return whether user is authenticated
     */
    public boolean isAuthenticated() {
        return accountId != null;
    }

    /**
     * @return get authenticated userId
     */
    @NotNull
    public String getAccountId() {
        if (accountId == null) throw new ForbiddenException();
        return accountId;
    }

    /**
     * @return user latLng
     */
    @Nullable
    public String getLatLng() {
        return latLng;
    }

    /**
     * @return user zoneId
     */
    @Nullable
    public ZoneId getZoneId() {
        return zoneId;
    }

    @Nullable
    public AuthenticatedToken getToken() {
        return token;
    }

    /**
     * @return Optional String UserId if exist
     */
    public Optional<String> accountId() {
        return Optional.ofNullable(accountId);
    }

    /**
     * LatLng is not validated but returned if exists
     * If exist, it should be formatted as such lat,lng
     * e.g. "1.34,103.8"
     *
     * @return Optional String of User Latitude Longitude if exists
     */
    public Optional<String> latLng() {
        return Optional.ofNullable(latLng);
    }

    /**
     * LocalDateTime is validated and returned if exists
     * Using Format: ISO_LOCAL_DATE_TIME
     * Example: '2011-12-03T10:15:30'
     *
     * @return Optional String of User Local Time if exists
     */
    public Optional<LocalDateTime> localDateTime() {
        if (zoneId == null) return Optional.empty();
        return Optional.of(LocalDateTime.now(zoneId));
    }
}
