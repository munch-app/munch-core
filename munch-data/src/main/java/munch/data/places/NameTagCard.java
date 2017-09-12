package munch.data.places;

import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 6/9/2017
 * Time: 12:50 AM
 * Project: munch-core
 */
public final class NameTagCard extends PlaceCard {

    private String name;
    private Set<String> tags;

    @Override
    public String getId() {
        return "basic_NameTag_12092017";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}
