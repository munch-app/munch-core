package munch.api.search.plugin;

import munch.api.search.cards.SearchHeaderCard;
import munch.api.search.cards.SearchNavigationHeader;

import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 26/10/18
 * Time: 1:22 PM
 * Project: munch-core
 */
@Singleton
public final class SearchBetweenLoader implements SearchCardPlugin {

    @Override
    public List<Position> load(Request request) {
        if (!request.getRequest().isBetween()) return null;

        List<SearchHeaderCard> headers = request.getCards().stream()
                .filter(searchCard -> searchCard instanceof SearchHeaderCard)
                .map(card -> (SearchHeaderCard) card)
                .collect(Collectors.toList());
        if (headers.isEmpty()) return null;

        SearchNavigationHeader card = new SearchNavigationHeader(headers);
        card.setTitle("Ideal EatBetween Locations");
        return of(-1, card);
    }
}