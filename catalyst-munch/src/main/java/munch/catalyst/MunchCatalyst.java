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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    private Date updatedDate;

    @Inject
    public MunchCatalyst(DataClient dataClient, CatalystClient catalystClient,
                         PlaceClient placeClient, ArticleClient articleClient, GalleryClient galleryClient) {
        super(logger, dataClient, catalystClient);
        this.placeClient = placeClient;
        this.articleClient = articleClient;
        this.galleryClient = galleryClient;
    }

    @Override
    protected void preStart() {
        super.preStart();
        this.updatedDate = new Date();
    }

    @Override
    protected void process(String catalystId) {
        List<CatalystLink> collected = new ArrayList<>();
        dataClient.getLinks(catalystId).forEachRemaining(collected::add);

        // Failed validation: will be deleted at postCycle
        if (!validate(collected)) return;

        // Else validate = success: put new place
        PlaceBuilder builder = new PlaceBuilder();
        Date updatedDate = new Timestamp(System.currentTimeMillis());

        // Consume for Article & Gallery and Place builder
        for (CatalystLink link : collected) {
            consume(link, updatedDate);
            builder.consume(link);
        }

        // Put place data to place services
        placeClient.put(builder.build());

        // Delete data that is not updated
        articleClient.deleteBefore(catalystId, updatedDate);
        galleryClient.deleteBefore(catalystId, updatedDate);
    }

    /**
     * Validate a collection of links to make sure it can be a munch place
     *
     * @param links links
     * @return true = validated, false = cannot be a munch place
     */
    private boolean validate(List<CatalystLink> links) {
        // TODO validate place with
        // 1. NEA Licence
        // 2. Blogger mentions
        return false;
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

    @Override
    protected void postCycle() {
        placeClient.deleteBefore(updatedDate);
    }
}
