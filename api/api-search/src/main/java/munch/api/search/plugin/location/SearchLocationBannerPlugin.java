package munch.api.search.plugin.location;

import munch.api.search.SearchQuery;
import munch.api.search.cards.SearchLocationBannerCard;
import munch.api.search.plugin.SearchCardPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 10/12/18
 * Time: 3:06 PM
 * Project: munch-core
 */
@Singleton
public final class SearchLocationBannerPlugin implements SearchCardPlugin {
    private static final Logger logger = LoggerFactory.getLogger(SearchLocationBannerPlugin.class);

    @Nullable
    @Override
    public List<Position> load(Request request) {
        if (!request.isFirstPage()) return null;
        if (!request.getRequest().isFeature(SearchQuery.Feature.Location)) return null;

        return of(-10, new SearchLocationBannerCard());
    }
}
