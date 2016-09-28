package com.munch.core.struct.util.object;

/**
 * Created By: Fuxing Loh
 * Date: 28/9/2016
 * Time: 7:33 PM
 * Project: struct
 */
public interface StorageSetting {

    String getBucket();

    default String getRegion() {
        return "ap-southeast-1";
    }

}
