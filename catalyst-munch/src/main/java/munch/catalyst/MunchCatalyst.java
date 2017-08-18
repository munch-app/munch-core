package munch.catalyst;

import catalyst.CatalystEngine;
import catalyst.data.CatalystClient;
import com.google.inject.Inject;
import corpus.data.CorpusData;
import munch.catalyst.builder.ArticleBuilder;
import munch.catalyst.builder.ImageCacheResolver;
import munch.catalyst.builder.LocationBuilder;
import munch.catalyst.builder.PlaceBuilder;
import munch.catalyst.clients.DataClient;
import munch.catalyst.clients.SearchClient;
import munch.data.Article;
import munch.data.Location;
import munch.data.Place;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 31/5/2017
 * Time: 5:11 AM
 * Project: munch-core
 */
public class MunchCatalyst extends CatalystEngine {
    private static final Logger logger = LoggerFactory.getLogger(MunchCatalyst.class);

    private final DataClient munchDataClient;
    private final SearchClient searchClient;
    private final ImageCacheResolver imageCacheResolver;

    private final PlaceIngress placeIngress = new PlaceIngress();
    private final LocationIngress locationIngress = new LocationIngress();

    private long cycleNo;

    @Inject
    public MunchCatalyst(catalyst.data.DataClient munchDataClient, CatalystClient catalystClient,
                         DataClient placeClient, SearchClient searchClient, ImageCacheResolver imageCacheResolver) {
        super(logger, munchDataClient, catalystClient);
        this.munchDataClient = placeClient;
        this.searchClient = searchClient;
        this.imageCacheResolver = imageCacheResolver;
    }

    @Override
    protected void preStart() {
        super.preStart();
        this.cycleNo = System.currentTimeMillis();
    }

    @Override
    protected void process(String catalystId) {
        List<CorpusData> collected = new ArrayList<>();
        dataClient.getLinked(catalystId).forEachRemaining(collected::add);

        placeIngress.ingress(collected);
        locationIngress.ingress(collected);
    }

    @Override
    protected void postCycle() {
        searchClient.deleteLocations(cycleNo);
        searchClient.deletePlaces(cycleNo);

        munchDataClient.deletePlaces(cycleNo);
        munchDataClient.deleteArticles(cycleNo);
    }

    private class PlaceIngress {
        void ingress(List<CorpusData> collected) {
            // Failed validation: will be deleted at postCycle
            if (!validate(collected)) return;

            // Else validate = success: put new place
            PlaceBuilder placeBuilder = new PlaceBuilder();
            ArticleBuilder articleBuilder = new ArticleBuilder(imageCacheResolver);
            Date updatedDate = new Timestamp(System.currentTimeMillis());

            // Consume for Article & Media and Place builder
            for (CorpusData data : collected) {
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
            } else logger.warn("Place unable to put due to incomplete corpus data: {}", collected);
        }

        /**
         * Validate a collection of links to make sure it can be a munch place
         * Validated with conditions:
         * 1. NEA Licence
         * 2. Blogger mentions
         *
         * @param links links
         * @return true = validated, false = cannot be a munch place
         */
        boolean validate(List<CorpusData> links) {
            // Make sure have at least 2 links
            if (links.size() < 2) return false;

            // Validate has at least 1 Article written about place
            if (links.stream().noneMatch(data -> ArticleBuilder.CorpusName
                    .matcher(data.getCorpusName()).matches())) return false;

            // Validate has NeaRecord
            return links.stream().anyMatch(l -> l.getCorpusName().equals("Sg.Nea.TrackRecord"));
        }
    }

    private class LocationIngress {
        void ingress(List<CorpusData> collected) {
            // Failed validation: will be deleted at postCycle
            if (!validate(collected)) return;

            // Else validate = success: put new place
            LocationBuilder locationBuilder = new LocationBuilder();
            Date updatedDate = new Timestamp(System.currentTimeMillis());
            collected.forEach(locationBuilder::consume);

            // Collect locations
            List<Location> locations = locationBuilder.collect(updatedDate);
            for (Location location : locations) {
                logger.info("Putting location id: {} name: {}", location.getId(), location.getName());
                searchClient.put(location, cycleNo);
            }
        }

        /**
         * Validate a collection of links to make sure it can be a munch place
         * Validated with conditions:
         * 1. NEA Licence
         * 2. Blogger mentions
         *
         * @param links links
         * @return true = validated, false = cannot be a munch place
         */
        boolean validate(List<CorpusData> links) {
            // As long any is LocationPolygon
            for (CorpusData link : links) {
                if (link.getCorpusName().equals("Sg.Munch.LocationPolygon")) return true;
            }
            return false;
        }
    }
}
