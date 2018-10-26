package munch.api.search.cards;

/**
 * Created by: Fuxing
 * Date: 20/1/2018
 * Time: 6:05 PM
 * Project: munch-core
 */
public final class SearchHeaderCard implements SearchCard {

    private String title;

    public SearchHeaderCard() {
    }

    public SearchHeaderCard(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getCardId() {
        return "injected_Header_20180120";
    }

    @Override
    public String getUniqueId() {
        return String.valueOf(System.currentTimeMillis());
    }
}
