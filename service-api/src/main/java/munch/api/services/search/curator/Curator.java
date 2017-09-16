package munch.api.services.search.curator;

import com.rits.cloning.Cloner;
import munch.api.clients.SearchClient;
import munch.api.services.search.cards.CardParser;
import munch.api.services.search.cards.SearchCollection;
import munch.data.SearchQuery;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 1/7/2017
 * Time: 10:21 PM
 * Project: munch-core
 */
public abstract class Curator {
    protected static final Cloner cloner = new Cloner();

    @Inject
    protected SearchClient searchClient;

    @Inject
    protected CardParser cardParser;

    /**
     * @param query  mandatory query in search bar
     * @return true if current curator can return results
     */
    public abstract boolean match(SearchQuery query);

    /**
     * @param query  mandatory query in search bar
     * @return Curated List of PlaceCollection
     */
    public abstract List<SearchCollection> curate(SearchQuery query);

    /**
     * @param query query to clone
     * @return deep cloned query
     */
    public static SearchQuery clone(SearchQuery query) {
        return cloner.deepClone(query);
    }
}
