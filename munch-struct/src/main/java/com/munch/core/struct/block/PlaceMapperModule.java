package com.munch.core.struct.block;

import com.google.inject.AbstractModule;
import com.munch.core.essential.block.GsonConverter;
import com.munch.core.essential.block.JsonConverter;

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

}
