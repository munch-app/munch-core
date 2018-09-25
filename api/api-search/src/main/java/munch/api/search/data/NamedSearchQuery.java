package munch.api.search.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by: Fuxing
 * Date: 25/9/18
 * Time: 9:36 AM
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class NamedSearchQuery {
    private String name;
    private String description;
    private String keywords;
    private String qid;
    private SearchQuery searchQuery;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public SearchQuery getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(SearchQuery searchQuery) {
        this.searchQuery = searchQuery;
    }

    @Override
    public String toString() {
        return "NamedSearchQuery{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", keywords='" + keywords + '\'' +
                ", qid='" + qid + '\'' +
                ", searchQuery=" + searchQuery +
                '}';
    }
}
