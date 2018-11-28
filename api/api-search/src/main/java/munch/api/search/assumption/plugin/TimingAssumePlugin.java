package munch.api.search.assumption.plugin;

import munch.api.search.SearchRequest;
import munch.api.search.SearchQuery;
import munch.api.search.assumption.Assumption;
import munch.data.Hour;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by: Fuxing
 * Date: 15/9/18
 * Time: 1:36 AM
 * Project: munch-core
 */
public final class TimingAssumePlugin extends AssumePlugin {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    protected static final Consumer<SearchRequest> ASSUMPTION_OPEN_NOW = request -> {
        LocalDateTime dateTime = request.getLocalTime();
        if (dateTime == null) return;

        SearchQuery query = request.getSearchQuery();
        if (query.getFilter() == null) query.setFilter(new SearchQuery.Filter());
        SearchQuery.Filter.Hour hour = new SearchQuery.Filter.Hour();

        hour.setType(SearchQuery.Filter.Hour.Type.OpenNow);
        hour.setDay(Hour.Day.parse(dateTime.getDayOfWeek()).name());
        hour.setOpen(dateTime.format(TIME_FORMATTER));

        if (dateTime.getHour() == 23) {
            hour.setClose("23:59");
        } else {
            LocalDateTime closeTime = dateTime.plusMinutes(30);
            hour.setClose(closeTime.format(TIME_FORMATTER));
        }

        query.getFilter().setHour(hour);
    };


    @Override
    public List<Assumption> get() {
        // Add More timing type
        return List.of(
                Assumption.of(Assumption.Type.Timing, "open now", "Open Now", ASSUMPTION_OPEN_NOW)
        );
    }
}
