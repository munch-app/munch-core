package com.instagram.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Date: 3/10/19
 * Time: 7:52 pm
 *
 * @author Fuxing Loh
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class InstagramPagination {

    @JsonProperty("next_max_id")
    private String nextMaxId;

    public String getNextMaxId() {
        return nextMaxId;
    }

    public void setNextMaxId(String nextMaxId) {
        this.nextMaxId = nextMaxId;
    }
}
