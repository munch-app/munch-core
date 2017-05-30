package munch.catalyst;

import catalyst.CatalystEngine;
import catalyst.data.CatalystClient;
import catalyst.data.CatalystLink;
import catalyst.data.DataClient;
import com.google.inject.Inject;
import munch.catalyst.clients.ArticleClient;
import munch.catalyst.clients.GalleryClient;
import munch.catalyst.clients.PlaceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by: Fuxing
 * Date: 31/5/2017
 * Time: 5:11 AM
 * Project: munch-core
 */
public class MunchCatalyst extends CatalystEngine {
    private static final Logger logger = LoggerFactory.getLogger(MunchCatalyst.class);

    private static final Pattern ArticleCorpusName = Pattern.compile("Global\\.Article\\.\\w+");
    private static final String GalleryCorpusName = "Global.Instagram.Media";

    private final PlaceClient placeClient;
    private final ArticleClient articleClient;
    private final GalleryClient galleryClient;

    @Inject
    public MunchCatalyst(DataClient dataClient, CatalystClient catalystClient,
                         PlaceClient placeClient, ArticleClient articleClient, GalleryClient galleryClient) {
        super(logger, dataClient, catalystClient);
        this.placeClient = placeClient;
        this.articleClient = articleClient;
        this.galleryClient = galleryClient;
    }

    @Override
    protected void process(String catalystId) {
        PlaceBuilder builder = new PlaceBuilder();
        Date updatedDate = new Timestamp(System.currentTimeMillis());

        dataClient.getLinks(catalystId).forEachRemaining(link -> {
            consume(link, updatedDate);
            builder.consume(link);
        });

        // Put place data to place services
        placeClient.put(builder.build());
        // Delete data that is not updated
        articleClient.deleteBefore(catalystId, updatedDate);
        galleryClient.deleteBefore(catalystId, updatedDate);
    }

    /**
     * Consume special catalyst link
     * Client will be in charge of structuring data for service.
     *
     * @param link link
     */
    private void consume(CatalystLink link, Date updatedDate) {
        String name = link.getData().getCorpusName();
        if (GalleryCorpusName.equals(name)) {
            galleryClient.put(link, updatedDate);
        } else if (ArticleCorpusName.matcher(name).matches()) {
            articleClient.put(link, updatedDate);
        }
    }
}
