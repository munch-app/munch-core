package munch.permutation;

import catalyst.utils.DateCompareUtils;
import com.google.common.collect.Iterators;
import munch.api.search.named.NamedQueryDatabase;
import munch.api.search.data.NamedSearchQuery;
import munch.api.search.data.SearchQuery;
import munch.data.client.AreaClient;
import munch.data.client.LandmarkClient;
import munch.data.client.TagClient;

import javax.inject.Inject;
import java.time.Duration;
import java.util.Iterator;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 30/10/18
 * Time: 9:24 AM
 * Project: munch-core
 */
public abstract class PermutationEngine implements NamedQueryMapper {
    protected NamedQueryDatabase database;

    protected APISearchClient apiSearchClient;
    protected TagClient tagClient;
    protected AreaClient areaClient;
    protected LandmarkClient landmarkClient;

    @Inject
    void inject(NamedQueryDatabase database, APISearchClient apiSearchClient, TagClient tagClient, AreaClient areaClient, LandmarkClient landmarkClient) {
        this.database = database;
        this.apiSearchClient = apiSearchClient;
        this.tagClient = tagClient;
        this.areaClient = areaClient;
        this.landmarkClient = landmarkClient;
    }

    @SuppressWarnings("ConstantConditions")
    public Iterator<NamedSearchQuery> get() {
        Iterator<SearchQuery> iterator = Iterators.filter(iterator(), Objects::nonNull);
        Iterator<NamedSearchQuery> mapped = Iterators.transform(iterator, this::map);

        mapped = Iterators.transform(mapped, namedQuery -> {
            NamedSearchQuery existing = database.get(namedQuery.getName());
            if (isUseExisting(existing, namedQuery)) return existing;

            namedQuery.setCount(apiSearchClient.count(namedQuery.getSearchQuery()));
            if (validate(namedQuery)) {
                database.put(namedQuery);
                return namedQuery;
            }
            return null;
        });

        return Iterators.filter(mapped, Objects::nonNull);
    }

    private boolean isUseExisting(NamedSearchQuery existing, NamedSearchQuery mapped) {
        if (existing == null) return false;
        if (DateCompareUtils.after(existing.getUpdatedMillis(), Duration.ofDays(21))) return false;
        if (existing.equals(mapped)) return true;
        return false;
    }

    /**
     * @return iterator of SearchQuery to create permutation with, MUST be non duplicate
     */
    protected abstract Iterator<SearchQuery> iterator();

    /**
     * @param namedSearchQuery query to validate
     * @return validation result
     */
    protected abstract boolean validate(NamedSearchQuery namedSearchQuery);
}
