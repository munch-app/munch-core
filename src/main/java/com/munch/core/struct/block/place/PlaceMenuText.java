package com.munch.core.struct.block.place;

import com.munch.core.essential.block.BlockVersion;

/**
 * Created By: Fuxing Loh
 * Date: 1/10/2016
 * Time: 8:43 PM
 * Project: struct
 */
public class PlaceMenuText extends BlockVersion {

    public static final String BUCKET_NAME = "munch.place.PlaceMenuText";

    private String menuId;
    private String keyId;

    private String text;

    /**
     * All data should be named with the version that is introduced
     * For data not named, it is there since VERSION_FIRST
     */
    public PlaceMenuText() {
        super(VERSION_FIRST);
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
