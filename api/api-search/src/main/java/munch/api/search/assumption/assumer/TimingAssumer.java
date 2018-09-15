package munch.api.search.assumption.assumer;

import munch.api.search.SearchRequest;
import munch.api.search.data.SearchQuery;
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
public final class TimingAssumer extends Assumer {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    protected static final Consumer<SearchRequest> ASSUMPTION_OPEN_NOW = request -> {
        LocalDateTime dateTime = request.getLocalTime();
        if (dateTime == null) return;

        SearchQuery query = request.getSearchQuery();
        if (query.getFilter() == null) query.setFilter(new SearchQuery.Filter());
        SearchQuery.Filter.Hour hour = new SearchQuery.Filter.Hour();

        hour.setName("Open Now");
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
