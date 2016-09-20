package com.munch.core.struct.block.website;

import com.munch.core.struct.block.BlockVersion;

import java.util.*;

/**
 * Created By: Fuxing Loh
 * Date: 26/8/2016
 * Time: 9:32 PM
 * Project: struct
 */
public class SourceWebPage extends BlockVersion {

    private String url;
    private List<String> content = new ArrayList<>();
    private String title;

    private Set<String> outUrls = new HashSet<>();
    private Map<String, String> metaTags = new HashMap<>();


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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<String> getOutUrls() {
        return outUrls;
    }

    public void setOutUrls(Set<String> outUrls) {
        this.outUrls = outUrls;
    }

    public Map<String, String> getMetaTags() {
        return metaTags;
    }

    public void setMetaTags(Map<String, String> metaTags) {
        this.metaTags = metaTags;
    }
}
