package munch.api;

import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;
import munch.restful.server.JsonTransformer;
import munch.restful.server.jwt.TokenAuthenticator;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 5:04 PM
 * Project: munch-core
 */
public abstract class ApiService implements JsonService {

    private WhitelistTransformer transformer;
    protected TokenAuthenticator<?> authenticator;

    @Inject
    void injectWhitelist(WhitelistTransformer transformer, TokenAuthenticator authenticator) {
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
     * @param call JsonCall
     * @return UserId
     */
    @NotNull
    public String getUserId(JsonCall call) {
        return authenticator.getSubject(call);
    }

    @Override
    public JsonTransformer toJson() {
        return transformer;
    }

}
