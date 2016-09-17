package com.munch.core.struct.block.source;

import com.munch.core.essential.json.AwsPersistClient;
import com.munch.core.essential.json.BlockMapper;
import com.munch.core.essential.json.GsonConverter;
import com.munch.core.essential.json.PersistClient;

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
        SeedPlace seedPlace = tool.get("8a80cb8157381d0f0157381d6d0701c0");
    }

    public SeedPlace get(String id) {
        return blockMapper.load(SeedPlace.class, id);
    }

    public String getString(String id) {
        return persistClient.load(id);
    }

}