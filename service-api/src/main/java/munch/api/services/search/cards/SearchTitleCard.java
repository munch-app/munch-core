package munch.api.services.search.cards;

import org.apache.commons.lang3.RandomUtils;

/**
 * Created by: Fuxing
 * Date: 11/12/2017
 * Time: 4:00 PM
 * Project: munch-core
 */
public final class SearchTitleCard implements SearchCard {

    private String title;

    @Override
    public String getCardId() {
        return "injected_Title_20171211";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getUniqueId() {
        return getTitle() + RandomUtils.nextInt(0, 1_000_000);
    }
}
