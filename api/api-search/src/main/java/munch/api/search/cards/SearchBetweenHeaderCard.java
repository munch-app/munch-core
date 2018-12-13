package munch.api.search.cards;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 2/11/18
 * Time: 6:04 AM
 * Project: munch-core
 */
public final class SearchBetweenHeaderCard implements SearchCard {

    private String title;
    private String description;
    private List<Anchor> anchors;

    public SearchBetweenHeaderCard(List<SearchHeaderCard> headerCards) {
        this.anchors = headerCards.stream()
                .map(card -> {
                    Anchor anchor = new Anchor();
                    anchor.setTitle(card.getTitle());
                    anchor.setUniqueId(card.getUniqueId());
                    return anchor;
                }).collect(Collectors.toList());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Anchor> getAnchors() {
        return anchors;
    }

    public void setAnchors(List<Anchor> anchors) {
        this.anchors = anchors;
    }

    @Override
    public String getCardId() {
        return "BetweenHeader_2018-12-13";
    }

    @Override
    public String getUniqueId() {
        return getCardId();
    }

    public static class Anchor {
        private String title;
        private String uniqueId;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUniqueId() {
            return uniqueId;
        }

        public void setUniqueId(String uniqueId) {
            this.uniqueId = uniqueId;
        }
    }
}
