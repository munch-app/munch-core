package com.munch.core.struct.block;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.munch.core.essential.block.AwsPersistClient;
import com.munch.core.essential.block.GsonConverter;
import com.munch.core.essential.block.JsonConverter;
import com.munch.core.essential.block.MigrateBlockMapper;
import com.munch.core.struct.block.place.PlaceMenuText;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created By: Fuxing Loh
 * Date: 1/10/2016
 * Time: 8:48 PM
 * Project: struct
 */
public class PlaceMapperModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(JsonConverter.class).to(GsonConverter.class);
    }

    @Provides
    @Named(PlaceMenuText.BUCKET_NAME)
    @Singleton
    MigrateBlockMapper<PlaceMenuText> provideMenuTextMapper(GsonConverter jsonConverter) {
        return new MigrateBlockMapper<>(PlaceMenuText.class, PlaceMenuText::getMenuId,
                new AwsPersistClient(PlaceMenuText.BUCKET_NAME), jsonConverter, new PlaceMenuText.Migration());
    }
}
