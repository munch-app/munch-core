package munch.catalyst;

import catalyst.CatalystEngine;
import catalyst.data.CatalystClient;
import catalyst.data.CorpusData;
import catalyst.data.DataClient;
import com.google.inject.Inject;
import munch.catalyst.builder.ArticleBuilder;
import munch.catalyst.builder.MediaBuilder;
import munch.catalyst.builder.place.ImageCurator;
import munch.catalyst.builder.place.PlaceBuilder;
import munch.catalyst.clients.ArticleClient;
import munch.catalyst.clients.InstagramClient;
import munch.catalyst.clients.PlaceClient;
import munch.catalyst.data.Article;
import munch.catalyst.data.InstagramMedia;
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
    private final ArticleClient articleClient;
    private final InstagramClient instagramClient;

    private Date updatedDate;

    @Inject
    public MunchCatalyst(DataClient dataClient, CatalystClient catalystClient,
                         PlaceClient placeClient, ArticleClient articleClient, InstagramClient instagramClient) {
        super(logger, dataClient, catalystClient);
        this.placeClient = placeClient;
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
            Place place = places.get(0);
            logger.info("Putting place id: {} name: {}", place.getId(), place.getName());
            // Put to 3 services
            medias = medias.stream().map(instagramClient::put).collect(Collectors.toList());
            articles = articles.stream().map(articleClient::put).collect(Collectors.toList());

            // Add images to place from medias and articles
            place.setImages(ImageCurator.selectFrom(medias, articles));
            placeClient.put(place);
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
    private boolean validate(List<CorpusData> links) {
        // Make sure have at least 2 links
        if (links.size() < 2) return false;

        // Validate has at least 1 Article written about place
        if (links.stream().noneMatch(data -> ArticleBuilder.CorpusName
                .matcher(data.getCorpusName()).matches())) return false;

        // Validate has NeaRecord
        return links.stream().anyMatch(l -> l.getCorpusName().equals("Sg.Nea.TrackRecord"));
    }

    @Override
    protected void postCycle() {
        placeClient.deleteBefore(updatedDate);
    }
}
