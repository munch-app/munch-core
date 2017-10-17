package munch.api.services.places.cards;

import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 6/9/2017
 * Time: 12:50 AM
 * Project: munch-core
 */
public final class PlaceNameTagCard extends AbstractPlaceCard {
    private String name;
    private Set<String> tags;

    @Override
    public String getCardId() {
        return "basic_NameTag_20170912";
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
