package munch.api.search.cards;

import munch.user.data.CreatorContent;
import munch.user.data.CreatorSeries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 2019-02-25
 * Time: 09:31
 * Project: munch-core
 */
public final class SearchSeriesListCard implements SearchCard {

    private final CreatorSeries series;
    private final List<CreatorContent> contents;
    private final Map<String, Object> options;

    public SearchSeriesListCard(CreatorSeries series, List<CreatorContent> contents) {
        this.series = series;
        this.contents = contents;
        this.options = new HashMap<>();
    }

    public CreatorSeries getSeries() {
        return series;
    }

    public List<CreatorContent> getContents() {
        return contents;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    @Override
    public String getCardId() {
        return "SeriesList_2019-02-25";
    }

    @Override
    public String getUniqueId() {
        return series.getSeriesId();
    }
}
