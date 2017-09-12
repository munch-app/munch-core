package munch.api.services.discovery;

import com.google.inject.Singleton;
import munch.data.SearchCollection;
import munch.data.SearchQuery;
import munch.data.SearchResult;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 11/7/2017
 * Time: 11:33 AM
 * Project: munch-core
 */
@Singleton
public final class SearchCurator extends Curator {

    /**
     * Check if a SearchQuery is complex
     * Only filters and query is checked
     * For filters:
     * Implicit user settings filters is considered non complex
     *
     * @param query query to check if empty
     * @return true if complex
     */
    @Override
    public boolean match(SearchQuery query) {
        if (StringUtils.isNotBlank(query.getQuery())) return true;
        if (query.getFilter() == null) return false;

        if (query.getFilter().getPrice() != null) {
            if (query.getFilter().getPrice().getMin() != null) return true;
            if (query.getFilter().getPrice().getMax() != null) return true;
        }

        if (query.getFilter().getTag() != null) {
            if (query.getFilter().getTag().getPositives() != null) {
                if (!query.getFilter().getTag().getPositives().isEmpty()) return true;
            }

            if (query.getFilter().getTag().getNegatives() != null) {
                if (!query.getFilter().getTag().getNegatives().isEmpty()) return true;
            }
        }

        if (query.getFilter().getHour() != null) {
            if (StringUtils.isNoneBlank(query.getFilter().getHour().getDay(),
                    query.getFilter().getHour().getTime())) return true;
        }

        return false;
    }

    @Override
    public List<SearchCollection> curate(SearchQuery query) {
        List<SearchResult> result = searchClient.search(query);
        // Wrap result into single collection
        return Collections.singletonList(new SearchCollection(null, query, result));
    }
}
