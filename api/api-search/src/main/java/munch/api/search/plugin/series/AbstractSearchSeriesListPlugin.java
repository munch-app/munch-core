package munch.api.search.plugin.series;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import munch.api.search.cards.SearchSeriesListCard;
import munch.api.search.plugin.SearchCardPlugin;
import munch.user.client.CreatorContentClient;
import munch.user.client.CreatorProfileClient;
import munch.user.client.CreatorSeriesClient;
import munch.user.client.CreatorSeriesContentClient;
import munch.user.data.CreatorContent;
import munch.user.data.CreatorProfile;
import munch.user.data.CreatorSeries;
import munch.user.data.CreatorSeriesContent;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static munch.user.data.CreatorSeriesContentIndex.sortId;

/**
 * Created by: Fuxing
 * Date: 2019-02-25
 * Time: 10:08
 * Project: munch-core
 */
public abstract class AbstractSearchSeriesListPlugin implements SearchCardPlugin {
    private static final Cache<Pair<String, Integer>, Optional<SearchSeriesListCard>> loadingCache;

    static {
        loadingCache = CacheBuilder.newBuilder()
                .expireAfterWrite(12, TimeUnit.HOURS)
                .build();
    }

    private CreatorSeriesClient seriesClient;
    private CreatorContentClient contentClient;
    private CreatorProfileClient profileClient;
    private CreatorSeriesContentClient seriesContentClient;

    @Inject
    public void inject(CreatorSeriesClient seriesClient, CreatorSeriesContentClient seriesContentClient, CreatorProfileClient profileClient, CreatorContentClient contentClient) {
        this.seriesClient = seriesClient;
        this.contentClient = contentClient;
        this.profileClient = profileClient;
        this.seriesContentClient = seriesContentClient;
    }

    @Nullable
    private SearchSeriesListCard loadFresh(String seriesId, int size) {
        CreatorSeries series = seriesClient.get(seriesId);
        if (series == null) return null;

        List<CreatorSeriesContent> seriesContents = seriesContentClient.list(sortId, seriesId, null, size);
        if (seriesContents.isEmpty()) return null;

        List<CreatorContent> contents = seriesContents.stream().map(creatorSeriesContent -> {
            String contentId = creatorSeriesContent.getContentId();
            return contentClient.get(contentId);
        }).filter(Objects::nonNull).collect(Collectors.toList());

        if (contents.isEmpty()) return null;

        series.setProfile(profileClient.get(series.getCreatorId()));
        return new SearchSeriesListCard(series, contents);
    }

    /**
     * @param seriesId to load
     * @return SearchSeriesListCard, cached
     */
    protected Optional<SearchSeriesListCard> load(String seriesId, int size) {
        try {
            return loadingCache.get(Pair.of(seriesId, size), () -> {
                SearchSeriesListCard card = loadFresh(seriesId, size);
                return Optional.ofNullable(card);
            });
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
