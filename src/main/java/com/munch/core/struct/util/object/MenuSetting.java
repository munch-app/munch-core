package com.munch.core.struct.util.object;

/**
 * Created By: Fuxing Loh
 * Date: 28/9/2016
 * Time: 9:19 PM
 * Project: struct
 */
public class MenuSetting implements StorageSetting {

    @Override
    public String getBucket() {
        return "munch.place.menus";
    }

}
