package com.munch.core.struct.block.website;

import com.munch.core.struct.block.BlockVersion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 26/8/2016
 * Time: 9:32 PM
 * Project: struct
 */
public class SourceWebPage extends BlockVersion {

    private String url;
    private List<String> content = new ArrayList<>();

    /**
     * All data should be named with the version that is introduced
     * For data not named, it is there since VERSION_FIRST
     */
    public SourceWebPage() {
        super(VERSION_FIRST);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }
}
