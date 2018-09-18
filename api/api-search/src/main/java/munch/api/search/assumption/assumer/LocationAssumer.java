package munch.api.search.assumption.assumer;

import munch.api.search.SearchRequest;
import munch.api.search.data.SearchQuery;
import munch.data.client.AreaClient;
import munch.data.location.Area;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by: Fuxing
 * Date: 15/9/18
 * Time: 1:35 AM
 * Project: munch-core
 */
@Singleton
public final class LocationAssumer extends Assumer {
    private static final List<Assumption> EXPLICITS = List.of(
            Assumption.of(Assumption.Type.Location, "nearby", "Nearby", applyArea(null)),
            Assumption.of(Assumption.Type.Location, "nearby me", "Nearby", applyArea(null)),
            Assumption.of(Assumption.Type.Location, "near me", "Near Me", applyArea(null)),
            Assumption.of(Assumption.Type.Location, "around me", "Around Me", applyArea(null))
    );

    private final AreaClient areaClient;

    @Inject
    public LocationAssumer(AreaClient areaClient) {
        this.areaClient = areaClient;
    }

    @Override
    public List<Assumption> get() {
        List<Assumption> assumptions = new ArrayList<>();
        Set<String> added = new HashSet<>();

        EXPLICITS.forEach(assumption -> {
            added.add(assumption.getToken());
            assumptions.add(assumption);
        });


        areaClient.iterator().forEachRemaining(area -> {
            String token = area.getName().toLowerCase();
            assumptions.add(Assumption.of(Assumption.Type.Location, token, area.getName(), applyArea(area)));
            added.add(token);

            for (String nameToken : area.getNames()) {
                nameToken = nameToken.toLowerCase();
                if (added.contains(nameToken)) continue;
                assumptions.add(Assumption.of(Assumption.Type.Location, nameToken, area.getName(), applyArea(area)));
            }
        });


        return assumptions;
    }

    public static Consumer<SearchRequest> applyArea(Area area) {
        return request -> {
            SearchQuery query = request.getSearchQuery();
            if (query.getFilter() == null) query.setFilter(new SearchQuery.Filter());
            query.getFilter().setArea(area);
        };
    }
}