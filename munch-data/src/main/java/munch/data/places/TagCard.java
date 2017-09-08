package munch.data.places;

import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 7/9/17
 * Time: 1:53 PM
 * Project: munch-core
 */
public final class TagCard extends PlaceCard {

    private Set<String> tags;

    @Override
    public String getId() {
        return "basic_Tag_07092017";
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}
