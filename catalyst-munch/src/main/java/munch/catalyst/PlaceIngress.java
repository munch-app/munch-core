package munch.catalyst;

import corpus.data.CorpusData;
import corpus.field.PlaceKey;
import munch.catalyst.builder.ArticleBuilder;
import munch.catalyst.builder.ImageCacheResolver;
import munch.catalyst.builder.PlaceBuilder;
import munch.catalyst.clients.DataClient;
import munch.catalyst.clients.SearchClient;
import munch.data.Article;
import munch.data.Place;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 20/8/2017
 * Time: 5:09 AM
 * Project: munch-core
 */
@Singleton
public final class PlaceIngress extends AbstractIngress {
    private static final Logger logger = LoggerFactory.getLogger(LocationIngress.class);

    private final SearchClient searchClient;
    private final DataClient munchDataClient;
    private final ImageCacheResolver imageCacheResolver;

    @Inject
    public PlaceIngress(SearchClient searchClient, DataClient munchDataClient, ImageCacheResolver imageCacheResolver) {
        this.searchClient = searchClient;
        this.munchDataClient = munchDataClient;
        this.imageCacheResolver = imageCacheResolver;
    }

    @Override
    protected void egress(long cycleNo) {
        searchClient.deletePlaces(cycleNo);
        munchDataClient.deletePlaces(cycleNo);
        munchDataClient.deleteArticles(cycleNo);
    }

    /**
     * Validate a collection of links to make sure it can be a munch place
     * Validated with conditions:
     * 1. NEA Licence
     * 2. Blogger mentions
     *
     * @param dataList links
     * @return true = validated, false = cannot be a munch place
     */
    protected boolean validate(List<CorpusData> dataList) {
        // Must have at least one image
        if (dataList.stream().noneMatch(PlaceKey.image::has)) return false;

        // Must have corpus: Sg.Nea.TrackRecord
        return hasCorpusName(dataList, "Sg.Nea.TrackRecord");
    }

    protected void put(List<CorpusData> dataList, final long cycleNo) {
        // Else validate = success: put new place
        PlaceBuilder placeBuilder = new PlaceBuilder();
        ArticleBuilder articleBuilder = new ArticleBuilder(imageCacheResolver);
        Date updatedDate = new Timestamp(System.currentTimeMillis());

        // Consume for Article & Media and Place builder
        for (CorpusData data : dataList) {
            placeBuilder.consume(data);
            articleBuilder.consume(data);
        }

        // Put place data to place services
        List<Place> places = placeBuilder.collect(updatedDate);
        List<Article> articles = articleBuilder.collect(updatedDate);

        if (!places.isEmpty()) {
            for (Place place : places) {
                logger.info("Putting place id: {} name: {}", place.getId(), place.getName());
                // Put to data and search services

                articles.forEach(article -> munchDataClient.put(article, cycleNo));

                munchDataClient.put(place, cycleNo);
                searchClient.put(place, cycleNo);
            }
        } else logger.warn("Place unable to put due to incomplete corpus data: {}", dataList);
    }
}
