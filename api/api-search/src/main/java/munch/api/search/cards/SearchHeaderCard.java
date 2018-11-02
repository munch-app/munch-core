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

    private String title;
    private String uniqueId;

    public SearchHeaderCard() {
    }

    public SearchHeaderCard(String title) {
        setTitle(title);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        if (uniqueId == null) {
            setUniqueId("h_" + KeyUtils.sha256Base64Url(title) + RandomStringUtils.randomAlphanumeric(3));
        }
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public String getCardId() {
        return "injected_Header_20180120";
    }

    @Override
    public String getUniqueId() {
        if (uniqueId == null) return getCardId();
        return uniqueId;
    }
}
