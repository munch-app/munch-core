package com.munch.core.struct.block.place;

import com.google.gson.JsonObject;
import com.munch.core.essential.block.BlockMigration;
import com.munch.core.essential.block.BlockVersion;
import com.munch.core.struct.rdbms.place.PlaceMenu;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 1/10/2016
 * Time: 8:43 PM
 * Project: struct
 */
public class PlaceMenuText extends BlockVersion {

    public static final String BUCKET_NAME = "munch.place.menu.text";

    /**
     * @see PlaceMenu#getId()
     */
    private String menuId;

    private List<String> textList;

    /**
     * All data should be named with the version that is introduced
     * For data not named, it is there since VERSION_FIRST
     */
    public PlaceMenuText() {
        super(VERSION_FIRST);
    }

    /**
     * @see PlaceMenu#getId()
     */
    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public List<String> getTextList() {
        return textList;
    }

    public void setTextList(List<String> textList) {
        this.textList = textList;
    }

    /**
     * Migration Module for PlaceMenu Text
     */
    public static class Migration extends BlockMigration<PlaceMenuText> {

        public Migration() {
            super(new PlaceMenuText());
        }

        @Override
        protected void update(JsonObject block) {
            throw new NotImplementedException("No updates implemented yet.");
        }

    }
}
