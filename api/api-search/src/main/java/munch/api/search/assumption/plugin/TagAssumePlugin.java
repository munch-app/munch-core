package munch.api.search.assumption.plugin;

import munch.api.search.SearchQuery;
import munch.api.search.SearchRequest;
import munch.api.search.assumption.Assumption;
import munch.data.client.TagClient;
import munch.data.tag.Tag;

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
 * Time: 1:36 AM
 * Project: munch-core
 */
@Singleton
public final class TagAssumePlugin {

    private final TagClient tagClient;

    @Inject
    public TagAssumePlugin(TagClient tagClient) {
        this.tagClient = tagClient;
    }

    public List<Assumption> get() {
        List<Assumption> assumptions = new ArrayList<>();
        Set<String> added = new HashSet<>();

        tagClient.iterator().forEachRemaining(tag -> {
            // Must be enabled for Assumptions
            if (!tag.getSearch().isEnabled()) return;

            String token = tag.getName().toLowerCase();
            assumptions.add(Assumption.of(Assumption.Type.Tag, token, tag.getName(), applyTag(tag)));
            added.add(token);

            // Tags that convert to another tag
            for (String nameToken : tag.getNames()) {
                nameToken = nameToken.toLowerCase();
                if (added.contains(nameToken)) continue;
                assumptions.add(Assumption.of(Assumption.Type.Tag, nameToken, tag.getName(), applyTag(tag)));
            }
        });
        return assumptions;
    }

    public static Consumer<SearchRequest> applyTag(Tag tag) {
        return request -> {
            SearchQuery query = request.getSearchQuery();
            if (query.getFilter() == null) query.setFilter(new SearchQuery.Filter());
            if (query.getFilter().getTags() == null) query.getFilter().setTags(new ArrayList<>());

            tag.setNames(null);
            tag.setPlace(null);
            tag.setCounts(null);
            tag.setSearch(null);
            tag.setCreatedMillis(null);
            tag.setUpdatedMillis(null);
            query.getFilter().getTags().add(tag);
        };
    }
}
