package com.munch.core.struct.block;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.munch.core.essential.json.*;
import com.munch.core.struct.block.source.SeedPlace;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created By: Fuxing Loh
 * Date: 22/9/2016
 * Time: 6:58 PM
 * Project: struct
 */
public class BlockMapperModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(JsonConverter.class).to(GsonConverter.class);
    }

    @Provides
    @Named(SeedPlace.BUCKET_NAME)
    @Singleton
    BlockMapper provideSeedPlaceBlockMapper(JsonConverter jsonConverter) {
        return new BlockMapper(new AwsPersistClient(SeedPlace.BUCKET_NAME), jsonConverter);
    }

    @Provides
    @Named(SeedPlace.BUCKET_NAME)
    @Singleton
    TypedBlockMapper<SeedPlace> provideSeedPlaceTypedMapper(@Named(SeedPlace.BUCKET_NAME) BlockMapper blockMapper) {
        return new TypedBlockMapper<>(SeedPlace.class, SeedPlace::getId, blockMapper);
    }

}
