package com.munch.core.struct.block.source;

import com.munch.core.essential.json.AwsPersistClient;
import com.munch.core.essential.json.BlockMapper;
import com.munch.core.essential.json.GsonConverter;
import com.munch.core.essential.json.PersistClient;
import com.munch.core.struct.block.website.SourceWebsite;

/**
 * Created by: Fuxing
 * Date: 18/9/2016
 * Time: 5:22 AM
 * Project: struct
 */
public class SeedPlaceReaderTool {

    private PersistClient persistClient;
    private BlockMapper blockMapper;

    public SeedPlaceReaderTool() {
        this.persistClient = new AwsPersistClient(SeedPlace.BUCKET_NAME);
        this.blockMapper = new BlockMapper(persistClient, new GsonConverter());
    }

    public static void main(String[] args) {
        SeedPlaceReaderTool tool = new SeedPlaceReaderTool();
        SeedPlace seedPlace = tool.get("8a80cb8157381d0f0157381d9c4602dc");
        System.out.println(seedPlace.getName());
        for (SourcePlace sourcePlace : seedPlace.getSourcePlaces()) {
            System.out.printf("Track Id: %s, Track Url: %s\n", sourcePlace.getTrackingId(), sourcePlace.getSourceUrl());
        }
        System.out.println("\n\n");
        for (SourceWebsite website : seedPlace.getSourceWebsites()) {
            System.out.printf("Seed Url: %s\n", website.getSeedUrl());
        }
    }

    public SeedPlace get(String id) {
        return blockMapper.load(SeedPlace.class, id);
    }

    public String getString(String id) {
        return persistClient.load(id);
    }

}