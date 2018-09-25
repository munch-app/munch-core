package munch.api.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.io.Resources;
import munch.api.search.data.NamedSearchQuery;
import munch.api.search.data.SearchQuery;
import munch.restful.core.JsonUtils;
import munch.user.client.UserSearchQueryClient;
import munch.user.data.UserSearchQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Optional;

/**
 * Created by: Fuxing
 * Date: 24/9/18
 * Time: 7:23 PM
 * Project: munch-core
 */
@Singleton
public final class NamedDelegator {
    private static final Logger logger = LoggerFactory.getLogger(NamedDelegator.class);

    private final UserSearchQueryClient userSearchQueryClient;
    private final Map<String, NamedSearchQuery> map;
    private final LoadingCache<String, Optional<SearchQuery>> cache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .build(new CacheLoader<>() {
                public Optional<SearchQuery> load(String qid) {
                    UserSearchQuery query = userSearchQueryClient.get(qid);
                    if (query == null) return Optional.empty();
                    SearchQuery searchQuery = JsonUtils.toObject(query.getSearchQuery(), SearchQuery.class);
                    return Optional.of(searchQuery);
                }
            });

    @Inject
    public NamedDelegator(UserSearchQueryClient userSearchQueryClient) throws IOException {
        this.userSearchQueryClient = userSearchQueryClient;
        URL url = Resources.getResource("named.json");
        JsonNode node = JsonUtils.objectMapper.readTree(url);
        this.map = JsonUtils.toMap(node, String.class, NamedSearchQuery.class);
    }

    /**
     * @param named to find
     * @return named SearchQuery or null
     */
    public NamedSearchQuery delegate(String named) {
        NamedSearchQuery namedSearchQuery = map.get(named);
        if (namedSearchQuery == null) return null;

        String qid = namedSearchQuery.getQid();
        Optional<SearchQuery> optional = cache.getUnchecked(qid);
        if (!optional.isPresent()) return null;
        namedSearchQuery.setSearchQuery(optional.get());
        return namedSearchQuery;
    }
}
