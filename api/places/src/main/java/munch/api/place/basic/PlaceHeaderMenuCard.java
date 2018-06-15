package munch.api.place.basic;

/**
 * Created by: Fuxing
 * Date: 13/3/18
 * Time: 5:06 PM
 * Project: munch-core
 */
public final class PlaceHeaderMenuCard extends AbstractPlaceCard {

    private String menuUrl;

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    @Override
    public String getCardId() {
        return "header_Menu_20180313";
    }
}
