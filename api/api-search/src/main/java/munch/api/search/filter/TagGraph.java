package munch.api.search.filter;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 28/11/18
 * Time: 4:15 AM
 * Project: munch-core
 */
public final class TagGraph {

    private List<FilterTag> tags;

    public List<FilterTag> getTags() {
        return tags;
    }

    public void setTags(List<FilterTag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "TagGraph{" +
                "tags=" + tags +
                '}';
    }

}
