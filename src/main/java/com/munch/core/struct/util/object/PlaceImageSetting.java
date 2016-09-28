package com.munch.core.struct.util.object;

/**
 * Created By: Fuxing Loh
 * Date: 28/9/2016
 * Time: 8:08 PM
 * Project: struct
 */
public class PlaceImageSetting implements StorageSetting {

    @Override
    public String getBucket() {
        return "munch.place.images";
    }

}
