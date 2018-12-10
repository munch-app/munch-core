package munch.api.search.cards;

import munch.restful.core.KeyUtils;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Created by: Fuxing
 * Date: 20/1/2018
 * Time: 6:05 PM
 * Project: munch-core
 */
public final class SearchHeaderCard implements SearchCard {

    private final String title;
    private final String uniqueId;
    private boolean sticky;

    public SearchHeaderCard(String title) {
        this.title = title;
        this.uniqueId = "h_" + KeyUtils.sha256Base64Url(title) + RandomStringUtils.randomAlphanumeric(3);
    }

    public String getTitle() {
        return title;
    }

    public boolean isSticky() {
        return sticky;
    }

    public void setSticky(boolean sticky) {
        this.sticky = sticky;
    }

    @Override
    public String getCardId() {
        return "Header_2018-11-29";
    }

    @Override
    public String getUniqueId() {
        if (uniqueId == null) return getCardId();
        return uniqueId;
    }
}
