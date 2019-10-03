package com.instagram.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Date: 3/10/19
 * Time: 7:50 pm
 *
 * @author Fuxing Loh
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class MediaRecentResponse {

    @JsonProperty("pagination")
    private InstagramPagination pagination;

    @JsonProperty("data")
    private List<InstagramMedia> data;

    public InstagramPagination getPagination() {
        return pagination;
    }

    public void setPagination(InstagramPagination pagination) {
        this.pagination = pagination;
    }

    public List<InstagramMedia> getData() {
        return data;
    }

    public void setData(List<InstagramMedia> data) {
        this.data = data;
    }
}
