package munch.api.search.assumption.assumer;

import munch.api.search.SearchRequest;
import munch.api.search.data.SearchQuery;
import munch.data.client.TagClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.function.Consumer;

/**
 * Created by: Fuxing
 * Date: 15/9/18
 * Time: 1:36 AM
 * Project: munch-core
 */
@Singleton
public final class TagAssumer {

    private final TagClient tagClient;

    @Inject
    public TagAssumer(TagClient tagClient) {
        this.tagClient = tagClient;
    }

    public List<Assumption> get() {
        List<Assumption> assumptions = new ArrayList<>();
        Set<String> added = new HashSet<>();

        tagClient.iterator().forEachRemaining(tag -> {
            String token = tag.getName().toLowerCase();
            assumptions.add(Assumption.of(Assumption.Type.Tag, token, tag.getName(), applyTag(tag.getName())));
            added.add(token);

            // Tags that convert to another tag
            for (String nameToken : tag.getNames()) {
                nameToken = nameToken.toLowerCase();
                if (added.contains(nameToken)) continue;
                assumptions.add(Assumption.of(Assumption.Type.Tag, nameToken, tag.getName(), applyTag(tag.getName())));
            }
        });
        return assumptions;
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
