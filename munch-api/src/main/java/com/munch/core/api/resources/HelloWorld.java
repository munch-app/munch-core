package com.munch.core.api.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by: Fuxing
 * Date: 6/11/2016
 * Time: 10:03 PM
 * Project: munch-core
 */
public class HelloWorld {

    private long id;
    private String content;

    public HelloWorld() {
    }

    public HelloWorld(long id, String content) {
        this.id = id;
        this.content = content;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    @JsonProperty
    public String getContent() {
        return content;
    }
}
