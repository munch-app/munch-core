package munch.api.services.discover.cards;

/**
 * Created by: Fuxing
 * Date: 1/5/18
 * Time: 10:26 AM
 * Project: munch-core
 */
public final class SearchPartnerInstagramCard implements SearchCard {

    // TODO Data Presentation

    @Override
    public String getCardId() {
        return "injected_PartnerInstagram_20180501";
    }

    @Override
    public String getUniqueId() {
        return getCardId();
    }
}
