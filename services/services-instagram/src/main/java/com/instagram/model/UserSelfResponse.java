package com.instagram.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Date: 3/10/19
 * Time: 7:50 pm
 *
 * @author Fuxing Loh
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class UserSelfResponse {

    @JsonProperty("data")
    private InstagramUser data;

    public InstagramUser getData() {
        return data;
    }

    public void setData(InstagramUser data) {
        this.data = data;
    }
}
