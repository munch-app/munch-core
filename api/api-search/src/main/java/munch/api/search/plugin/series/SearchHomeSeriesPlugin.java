package munch.api.search.plugin.series;

import munch.api.search.SearchQuery;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 2019-02-24
 * Time: 22:35
 * Project: munch-core
 */
@Singleton
public final class SearchHomeSeriesPlugin extends AbstractSearchSeriesListPlugin {

    @Override
    public List<Position> load(Request request) {
        if (!request.isFirstPage()) return null;
        if (!request.getRequest().isFeature(SearchQuery.Feature.Home)) return null;

        List<Position> positions = new ArrayList<>();

        // Top 10s
        load("675971ec-2003-46fe-aee9-cb15a692df61", 10).ifPresent(card -> {
            positions.add(ofPosition(-6, card));
        });

        // The Usual Suspects
        load("e0d039ae-64ea-4713-a999-3e90aadaaca4", 10).ifPresent(card -> {
            positions.add(ofPosition(10, card));
        });

        return positions;
    }
}
