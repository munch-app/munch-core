package munch.api.search.cards;

import munch.user.data.CreatorContent;
import munch.user.data.CreatorSeries;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 2019-02-25
 * Time: 09:31
 * Project: munch-core
 */
public final class SearchSeriesListCard implements SearchCard {

    private final CreatorSeries series;
    private final List<CreatorContent> contents;

    public SearchSeriesListCard(CreatorSeries series, List<CreatorContent> contents) {
        this.series = series;
        this.contents = contents;
    }

    public CreatorSeries getSeries() {
        return series;
    }

    public List<CreatorContent> getContents() {
        return contents;
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
