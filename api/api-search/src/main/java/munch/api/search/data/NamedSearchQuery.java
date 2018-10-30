package munch.api.search.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
    private String title;
    private String keywords;
    private String description;

    private SearchQuery searchQuery;

    private Long count;
    private Long updatedMillis;

    /**
     * @return name, unique slug of search query
     */
    @NotBlank
    @Size(min = 4, max = 255)
    @Pattern(regexp = "[a-z]{3}-[a-z0-9-]+")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotBlank
    @Size(min = 10, max = 255)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotBlank
    @Size(min = 10, max = 400)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotBlank
    @Size(min = 10, max = 400)
    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @NotNull
    public SearchQuery getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(SearchQuery searchQuery) {
        this.searchQuery = searchQuery;
    }

    @NotNull
    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @NotNull
    public Long getUpdatedMillis() {
        return updatedMillis;
    }

    public void setUpdatedMillis(Long updatedMillis) {
        this.updatedMillis = updatedMillis;
    }

    @Override
    public String toString() {
        return "NamedSearchQuery{" +
                "name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", keywords='" + keywords + '\'' +
                ", description='" + description + '\'' +
                ", searchQuery=" + searchQuery +
                ", count=" + count +
                ", updatedMillis=" + updatedMillis +
                '}';
    }
}
