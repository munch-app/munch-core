package munch.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by: Fuxing
 * Date: 11/7/2017
 * Time: 11:51 AM
 * Project: munch-core
 */
public interface SearchResult {

    @JsonProperty("_type")
    String getType();

}
