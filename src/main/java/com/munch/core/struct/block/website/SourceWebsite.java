package com.munch.core.struct.block.website;

import com.munch.core.struct.block.BlockVersion;

import java.util.HashSet;
import java.util.Set;

/**
 * Created By: Fuxing Loh
 * Date: 26/8/2016
 * Time: 9:28 PM
 * Project: struct
 */
public class SourceWebsite extends BlockVersion {

    private String seedUrl;
    private Set<SourceWebPage> webPages = new HashSet<>();
    private Set<String> urls = new HashSet<>();

    private Set<String> mediaUrls = new HashSet<>();
    private Set<String> pdfUrls = new HashSet<>();

    /**
     * All data should be named with the version that is introduced
     * For data not named, it is there since VERSION_FIRST
     */
    public SourceWebsite() {
        super(VERSION_FIRST);
    }

    public String getSeedUrl() {
        return seedUrl;
    }

    public void setSeedUrl(String seedUrl) {
        this.seedUrl = seedUrl;
    }

    public Set<SourceWebPage> getWebPages() {
        return webPages;
    }

    public void setWebPages(Set<SourceWebPage> webPages) {
        this.webPages = webPages;
    }

    public Set<String> getUrls() {
        return urls;
    }

    public void setUrls(Set<String> urls) {
        this.urls = urls;
    }

    public Set<String> getMediaUrls() {
        return mediaUrls;
    }

    public void setMediaUrls(Set<String> mediaUrls) {
        this.mediaUrls = mediaUrls;
    }

    public Set<String> getPdfUrls() {
        return pdfUrls;
    }

    public void setPdfUrls(Set<String> pdfUrls) {
        this.pdfUrls = pdfUrls;
    }
}
