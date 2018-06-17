package munch.api.search.assumption;

import com.google.inject.ImplementedBy;
import munch.api.search.SearchRequest;
import munch.api.search.data.SearchQuery;
import munch.data.Hour;
import munch.data.client.AreaClient;
import munch.data.client.TagClient;
import munch.data.location.Area;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by: Fuxing
 * Date: 23/2/18
 * Time: 8:50 PM
 * Project: munch-data
 */
@Singleton
@ImplementedBy(CachedAssumptionDatabase.class)
public class AssumptionDatabase {
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

    protected static final List<Assumption> EXPLICIT_ASSUMPTION = List.of(
            // Location Assumption
            Assumption.of(Assumption.Type.Location, "nearby", "Nearby", applyArea(null)),
            Assumption.of(Assumption.Type.Location, "nearby me", "Nearby", applyArea(null)),
            Assumption.of(Assumption.Type.Location, "near me", "Near Me", applyArea(null)),
            Assumption.of(Assumption.Type.Location, "around me", "Around Me", applyArea(null)),

            // Price Range Assumption
            // Future: Cheap, Budget, Expensive
            // From 50 to 60 Dollar
            // Under 70 Dollars

            // Timing Assumption
            // Add Open Now
            Assumption.of(Assumption.Type.Timing, "open now", "Open Now", ASSUMPTION_OPEN_NOW)
    );

    private final AreaClient areaClient;
    private final TagClient tagClient;

    @Inject
    public AssumptionDatabase(AreaClient areaClient, TagClient tagClient) {
        this.areaClient = areaClient;
        this.tagClient = tagClient;
    }

    public Map<String, Assumption> get() {
        Map<String, Assumption> assumptionMap = new HashMap<>();
        EXPLICIT_ASSUMPTION.forEach(assumption -> assumptionMap.put(assumption.getToken(), assumption));

        areaClient.iterator().forEachRemaining(area -> {
            String token = area.getName().toLowerCase();
            assumptionMap.putIfAbsent(token, Assumption.of(Assumption.Type.Location, token, area.getName(), applyArea(area)));
            for (String nameToken : area.getNames()) {
                nameToken = nameToken.toLowerCase();
                assumptionMap.putIfAbsent(nameToken, Assumption.of(Assumption.Type.Location, nameToken, area.getName(), applyArea(area)));
            }
        });

        tagClient.iterator().forEachRemaining(tag -> {
            String token = tag.getName().toLowerCase();
            assumptionMap.putIfAbsent(token, Assumption.of(Assumption.Type.Tag, token, tag.getName(), applyTag(tag.getName())));

            // Tags that convert to another tag
            for (String nameToken : tag.getNames()) {
                nameToken = nameToken.toLowerCase();
                assumptionMap.putIfAbsent(nameToken, Assumption.of(Assumption.Type.Tag, nameToken, tag.getName(), applyTag(tag.getName())));
            }
        });
        return assumptionMap;
    }

    public static Consumer<SearchRequest> applyArea(Area area) {
        return request -> {
            SearchQuery query = request.getSearchQuery();
            if (query.getFilter() == null) query.setFilter(new SearchQuery.Filter());
            query.getFilter().setArea(area);
        };
    }

    public static Consumer<SearchRequest> applyTag(String tag) {
        return request -> {
            SearchQuery query = request.getSearchQuery();
            if (query.getFilter() == null) query.setFilter(new SearchQuery.Filter());
            if (query.getFilter().getTag() == null) query.getFilter().setTag(new SearchQuery.Filter.Tag());
            if (query.getFilter().getTag().getPositives() == null)
                query.getFilter().getTag().setPositives(new HashSet<>());
            query.getFilter().getTag().getPositives().add(tag);
        };
    }
}
