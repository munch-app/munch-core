package munch.api;

import munch.restful.core.exception.AuthenticationException;
import munch.restful.core.exception.ConflictException;
import munch.restful.server.jwt.AuthenticatedToken;
import munch.user.data.UserProfile;
import spark.Request;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Created by: Fuxing
 * Date: 17/9/18
 * Time: 4:06 PM
 * Project: munch-core
 */
public final class ApiRequest {
    public static final String HEADER_USER_LAT_LNG = "User-Lat-Lng";
    public static final String HEADER_USER_ZONE_ID = "User-Zone-Id";

    private String userId;
    private UserProfile userProfile;
    private Supplier<UserProfile> profileSupplier;

    private String latLng;
    private ZoneId zoneId;
    private LocalDateTime localDateTime;

    ApiRequest(Request request, @Nullable AuthenticatedToken token, Supplier<UserProfile> profileSupplier) {
        this.profileSupplier = profileSupplier;

        if (token != null) {
            this.userId = token.getSubject();
        }

        latLng = request.headers(HEADER_USER_LAT_LNG);
        zoneId = parseZoneId(request.headers(HEADER_USER_ZONE_ID));

        if (zoneId != null) {
            localDateTime = LocalDateTime.now(zoneId);
        }
    }

    /**
     * @return whether user is authenticated
     */
    public boolean isAuthenticated() {
        return userId != null;
    }

    /**
     * @return get authenticated userId
     */
    @NotNull
    public String getUserId() {
        if (userId == null) throw new AuthenticationException(403, "Forbidden");
        return userId;
    }

    /**
     * @return UserProfile
     * @throws ConflictException if profile not found, but userId exists
     */
    @NotNull
    public UserProfile getUserProfile() throws ConflictException {
        if (userId == null) throw new AuthenticationException(403, "Forbidden");
        if (userProfile == null) {
            userProfile = profileSupplier.get();

            if (userProfile == null) {
                throw new ConflictException("Profile not found. (please report this error.)");
            }
        }
        return userProfile;
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

    /**
     * @return user local time
     */
    @Nullable
    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    /**
     * @return Optional String UserId if exist
     */
    public Optional<String> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    /**
     * @return Optional UserProfile, if UserId exist
     * @throws ConflictException if profile not found, but userId exists
     */
    public Optional<UserProfile> optionalUserProfile() throws ConflictException {
        if (userId == null) return Optional.empty();
        return Optional.of(getUserProfile());
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
    public Optional<LocalDateTime> optionalLocalDateTime() {
        return Optional.ofNullable(localDateTime);
    }

    @Nullable
    private static ZoneId parseZoneId(String zoneId) {
        // TODO(fuxing): Removed default ZoneId when implemented, for now defaults to Singapore
        if (zoneId == null) return ZoneId.of("Asia/Singapore");

        try {
            return ZoneId.of(zoneId);
        } catch (Exception e) {
            return null;
        }
    }
}
