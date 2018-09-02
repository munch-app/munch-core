package munch.api;

import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;
import munch.restful.server.JsonTransformer;
import munch.restful.server.jwt.TokenAuthenticator;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 5:04 PM
 * Project: munch-core
 */
public abstract class ApiService implements JsonService {
    public static final String HEADER_USER_LAT_LNG = "User-Lat-Lng";
    public static final String HEADER_USER_LOCAL_TIME = "User-Local-Time";

    private CleanerTransformer transformer;
    protected TokenAuthenticator<?> authenticator;

    /**
     * @param transformer   whitelist transformer to clean data
     * @param authenticator authenticator to authenticate user
     */
    @Inject
    void injectWhitelist(CleanerTransformer transformer, TokenAuthenticator authenticator) {
        this.transformer = transformer;
        this.authenticator = authenticator;
    }

    /**
     * @param call JsonCall
     * @return Optional String UserId if exist
     */
    public Optional<String> optionalUserId(JsonCall call) {
        return authenticator.optionalSubject(call);
    }

    /**
     * LatLng is not validated but returned if exists
     * If exist, it should be formatted as such lat,lng
     * e.g. "1.34,103.8"
     *
     * @param call JsonCall
     * @return Optional String of User Latitude Longitude if exists
     */
    public static Optional<String> optionalUserLatLng(JsonCall call) {
        return Optional.ofNullable(call.getHeader(HEADER_USER_LAT_LNG));
    }

    /**
     * LocalDateTime is validated and returned if exists
     * Using Format: ISO_LOCAL_DATE_TIME
     * Example: '2011-12-03T10:15:30'
     *
     * @param call JsonCall
     * @return Optional String of User Local Time if exists
     */
    public static Optional<LocalDateTime> optionalUserLocalTime(JsonCall call) {
        return Optional.ofNullable(call.getHeader(HEADER_USER_LOCAL_TIME))
                .map(LocalDateTime::parse);
    }

    /**
     * @param call JsonCall to authenticate
     * @return UserId
     */
    @NotNull
    public String getUserId(JsonCall call) {
        return authenticator.getSubject(call);
    }

    /**
     * @return Whitelist Json Transformer
     */
    @Override
    public JsonTransformer toJson() {
        return transformer;
    }

}
