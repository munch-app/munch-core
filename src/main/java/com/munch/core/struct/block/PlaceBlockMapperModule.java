package com.munch.core.struct.block;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.munch.core.essential.block.*;
import com.munch.core.struct.block.place.PlaceMenuText;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created By: Fuxing Loh
 * Date: 1/10/2016
 * Time: 8:48 PM
 * Project: struct
 */
public class PlaceBlockMapperModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(JsonConverter.class).to(GsonConverter.class);
    }

    @Provides
    @Named(PlaceMenuText.BUCKET_NAME)
    @Singleton
    BlockMapper provideSeedPlaceBlockMapper(JsonConverter jsonConverter) {
        return new BlockMapper(new AwsPersistClient(PlaceMenuText.BUCKET_NAME), jsonConverter);
    }

    @Provides
    @Named(PlaceMenuText.BUCKET_NAME)
    @Singleton
    TypedBlockMapper<PlaceMenuText> provideSeedPlaceTypedMapper(@Named(PlaceMenuText.BUCKET_NAME) BlockMapper blockMapper) {
        return new TypedBlockMapper<>(PlaceMenuText.class, PlaceMenuText::getMenuId, blockMapper);
    }
}
