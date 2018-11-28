package munch.permutation;

import com.google.common.collect.Iterators;
import munch.api.search.SearchQuery;
import munch.data.client.AreaClient;
import munch.data.client.LandmarkClient;
import munch.data.client.NamedQueryClient;
import munch.data.client.TagClient;
import munch.data.named.NamedQuery;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 30/10/18
 * Time: 9:24 AM
 * Project: munch-core
 */
public abstract class PermutationEngine implements NamedQueryMapper {
    protected NamedQueryClient namedQueryClient;

    protected APISearchClient apiSearchClient;
    protected TagClient tagClient;
    protected AreaClient areaClient;
    protected LandmarkClient landmarkClient;

    @Inject
    void inject(NamedQueryClient namedQueryClient, APISearchClient apiSearchClient, TagClient tagClient, AreaClient areaClient, LandmarkClient landmarkClient) {
        this.namedQueryClient = namedQueryClient;
        this.apiSearchClient = apiSearchClient;
        this.tagClient = tagClient;
        this.areaClient = areaClient;
        this.landmarkClient = landmarkClient;
    }

    @SuppressWarnings("ConstantConditions")
    public Iterator<NamedQuery> get() {
        Iterator<NamedQuery> mapped = Iterators.transform(iterator(), query -> {
            if (query == null) return null;

            NamedQuery namedQuery = create(query);
            if (!isValid(namedQuery)) return null;

            namedQueryClient.put(namedQuery);
            return namedQuery;
        });

        return Iterators.filter(mapped, Objects::nonNull);
    }

    private NamedQuery create(SearchQuery searchQuery) {
        NamedQuery namedQuery = new NamedQuery();
        namedQuery.setSlug(getSlug(searchQuery));
        namedQuery.setVersion(getVersion());
        namedQuery.setUpdatedMillis(System.currentTimeMillis());

        namedQuery.setTitle(getTitle(searchQuery));
        namedQuery.setDescription(getDescription(searchQuery));

        namedQuery.setAreas(searchQuery.getFilter().getLocation().getAreas());
        namedQuery.setTags(searchQuery.getFilter().getTags());
        namedQuery.setCount(apiSearchClient.count(searchQuery));

        return namedQuery;
    }


    /**
     * @return iterator of SearchQuery to create permutation with, MUST be non duplicate
     */
    protected abstract Iterator<SearchQuery> iterator();

    /**
     * @param namedQuery query to isValid
     * @return validation result
     */
    protected abstract boolean isValid(NamedQuery namedQuery);
}
