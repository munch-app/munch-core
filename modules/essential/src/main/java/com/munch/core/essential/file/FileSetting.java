package com.munch.core.essential.file;

/**
 * Created By: Fuxing Loh
 * Date: 28/9/2016
 * Time: 7:33 PM
 * Project: struct
 */
@FunctionalInterface
public interface FileSetting {

    String getBucket();

    default String getRegion() {
        return "ap-southeast-1";
    }

}
