package munch.api.search.plugin.location;

import munch.api.search.cards.SearchBetweenHeaderCard;
import munch.api.search.cards.SearchBetweenReferralCard;
import munch.api.search.cards.SearchHeaderCard;
import munch.api.search.plugin.SearchCardPlugin;

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
public final class SearchBetweenPlugin implements SearchCardPlugin {

    @Override
    public List<Position> load(Request request) {
        if (!request.getRequest().isBetween()) return null;
        if (!request.isFirstPage()) return null;

        List<SearchHeaderCard> headers = request.getCards().stream()
                .filter(searchCard -> searchCard instanceof SearchHeaderCard)
                .map(card -> (SearchHeaderCard) card)
                .collect(Collectors.toList());
        if (headers.isEmpty()) return null;

        return List.of(
                ofPosition(-1, new SearchBetweenHeaderCard(headers)),
                ofPosition(5, new SearchBetweenReferralCard())
        );
    }
}
