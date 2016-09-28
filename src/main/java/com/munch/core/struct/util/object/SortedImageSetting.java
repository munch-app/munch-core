package com.munch.core.struct.util.object;

/**
 * Created By: Fuxing Loh
 * Date: 28/9/2016
 * Time: 9:18 PM
 * Project: struct
 */
public class SortedImageSetting implements StorageSetting {

    @Override
    public String getBucket() {
        return "munch.images";
    }
}
