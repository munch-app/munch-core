package com.munch.core.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by: Fuxing
 * Date: 6/11/2016
 * Time: 9:39 PM
 * Project: munch-core
 */
public class MunchApiConfiguration extends Configuration {

    @Valid
    @NotNull
    private DataSourceFactory database;

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    public void setDatabase(DataSourceFactory database) {
        this.database = database;
    }
}
