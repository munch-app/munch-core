package com.instagram.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Date: 2/10/19
 * Time: 4:06 pm
 *
 * @author Fuxing Loh
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class AccessTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("user")
    private InstagramUser user;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public InstagramUser getUser() {
        return user;
    }

    public void setUser(InstagramUser user) {
        this.user = user;
    }
}
