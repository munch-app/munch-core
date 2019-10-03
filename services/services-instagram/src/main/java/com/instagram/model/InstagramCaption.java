package com.instagram.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Date: 3/10/19
 * Time: 9:07 pm
 *
 * @author Fuxing Loh
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class InstagramCaption {

    @JsonProperty("id")
    private String id;

    @JsonProperty("created_time")
    private Integer createdTime;

    @JsonProperty("text")
    private String text;

    @JsonProperty("from")
    private InstagramUser from;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Integer createdTime) {
        this.createdTime = createdTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public InstagramUser getFrom() {
        return from;
    }

    public void setFrom(InstagramUser from) {
        this.from = from;
    }
}
