package munch.api.search.cards;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 11/5/18
 * Time: 8:57 PM
 * Project: munch-core
 */
public final class SearchSuggestedTagCard implements SearchCard {

    private String locationName;
    private List<Tag> tags;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public static class Tag {
        private String name;
        private long count;

        public Tag(String name, long count) {
            this.name = name;
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }
    }

    @Override
    public String getCardId() {
        return "injected_SuggestedTag_20180511";
    }

    @Override
    public String getUniqueId() {
        return getCardId();
    }
}
