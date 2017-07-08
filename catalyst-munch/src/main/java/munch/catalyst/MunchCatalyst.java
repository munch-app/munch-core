package munch.catalyst;

import catalyst.CatalystEngine;
import catalyst.data.CatalystClient;
import catalyst.data.CorpusData;
import catalyst.data.DataClient;
import com.google.inject.Inject;
import munch.catalyst.builder.ArticleBuilder;
import munch.catalyst.builder.LocationBuilder;
import munch.catalyst.builder.MediaBuilder;
import munch.catalyst.builder.place.ImageCurator;
import munch.catalyst.builder.place.PlaceBuilder;
import munch.catalyst.clients.ArticleClient;
import munch.catalyst.clients.InstagramClient;
import munch.catalyst.clients.PlaceClient;
import munch.catalyst.clients.SearchClient;
import munch.catalyst.data.Article;
import munch.catalyst.data.InstagramMedia;
import munch.catalyst.data.Location;
import munch.catalyst.data.Place;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 31/5/2017
 * Time: 5:11 AM
 * Project: munch-core
 */
public class MunchCatalyst extends CatalystEngine {
    private static final Logger logger = LoggerFactory.getLogger(MunchCatalyst.class);

    private final PlaceClient placeClient;
    private final SearchClient searchClient;
    private final ArticleClient articleClient;
    private final InstagramClient instagramClient;

    private final PlaceIngress placeIngress = new PlaceIngress();
    private final LocationIngress locationIngress = new LocationIngress();

    private Date updatedDate;

    @Inject
    public MunchCatalyst(DataClient dataClient, CatalystClient catalystClient,
                         PlaceClient placeClient, SearchClient searchClient,
                         ArticleClient articleClient, InstagramClient instagramClient) {
        super(logger, dataClient, catalystClient);
        this.placeClient = placeClient;
        this.searchClient = searchClient;
        this.articleClient = articleClient;
        this.instagramClient = instagramClient;
    }

    @Override
    protected void preStart() {
        super.preStart();
        this.updatedDate = new Date();
    }

    @Override
    protected void process(String catalystId) {
        List<CorpusData> collected = new ArrayList<>();
        dataClient.getLinked(catalystId).forEachRemaining(collected::add);

        placeIngress.ingress(catalystId, collected);
        locationIngress.ingress(catalystId, collected);
    }

    @Override
    protected void postCycle() {
        searchClient.deleteBefore("locations", updatedDate);
        searchClient.deleteBefore("places", updatedDate);
        placeClient.deleteBefore(updatedDate);
    }

    public class PlaceIngress {
        void ingress(String catalystId, List<CorpusData> collected) {
            // Failed validation: will be deleted at postCycle
            if (!validate(collected)) return;

            // Else validate = success: put new place
            PlaceBuilder placeBuilder = new PlaceBuilder();
            ArticleBuilder articleBuilder = new ArticleBuilder();
            MediaBuilder mediaBuilder = new MediaBuilder();
            Date updatedDate = new Timestamp(System.currentTimeMillis());

            // Consume for Article & Media and Place builder
            for (CorpusData data : collected) {
                placeBuilder.consume(data);
                articleBuilder.consume(data);
                mediaBuilder.consume(data);
            }

            // Put place data to place services
            List<Place> places = placeBuilder.collect(updatedDate);
            List<InstagramMedia> medias = mediaBuilder.collect(updatedDate);
            List<Article> articles = articleBuilder.collect(updatedDate);

            if (!places.isEmpty()) {
                for (Place place : places) {
                    logger.info("Putting place id: {} name: {}", place.getId(), place.getName());
                    // Put to 3 services
                    medias = medias.stream().map(instagramClient::put).collect(Collectors.toList());
                    articles = articles.stream().map(articleClient::put).collect(Collectors.toList());

                    // Add images to place from medias and articles
                    place.setImages(ImageCurator.selectFrom(medias, articles));
                    placeClient.put(place);
                    searchClient.put(place);
                }
            } else logger.warn("Place unable to put due to incomplete corpus data: {}", collected);

            // Delete place associated data that is not updated
            articleClient.deleteBefore(catalystId, updatedDate);
            instagramClient.deleteBefore(catalystId, updatedDate);
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

    public class LocationIngress {
        void ingress(String catalystId, List<CorpusData> collected) {
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
                searchClient.put(location);
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
