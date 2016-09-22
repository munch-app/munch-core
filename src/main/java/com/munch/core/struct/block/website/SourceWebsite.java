package com.munch.core.struct.block.website;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.munch.core.struct.block.BlockVersion;
import com.munch.core.struct.block.source.SourceTag;

import java.beans.Transient;
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
    private Set<SourceTag> sourceTags = new HashSet<>();

    private int source;
    private int sourceCount; // How many times it appeared.

    // Source Confirmed Status
    private int sourceConfirmation;

    // For Dashboard to rank
    private double score;

    private long extractedCount;
    private long rawCount;

    private long tagCount;
    private long typeCount;

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

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getSourceConfirmation() {
        return sourceConfirmation;
    }

    public void setSourceConfirmation(int sourceConfirmation) {
        this.sourceConfirmation = sourceConfirmation;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Set<SourceTag> getSourceTags() {
        return sourceTags;
    }

    public void setSourceTags(Set<SourceTag> sourceTags) {
        this.sourceTags = sourceTags;
    }

    public long getExtractedCount() {
        return extractedCount;
    }

    public void setExtractedCount(long extractedCount) {
        this.extractedCount = extractedCount;
    }

    public long getRawCount() {
        return rawCount;
    }

    public void setRawCount(long rawCount) {
        this.rawCount = rawCount;
    }

    public long getTagCount() {
        return tagCount;
    }

    public void setTagCount(long tagCount) {
        this.tagCount = tagCount;
    }

    public long getTypeCount() {
        return typeCount;
    }

    public void setTypeCount(long typeCount) {
        this.typeCount = typeCount;
    }

    public int getSourceCount() {
        return sourceCount;
    }

    public void setSourceCount(int sourceCount) {
        this.sourceCount = sourceCount;
    }

    @Transient
    @JsonIgnore
    public SourceWebPage getRootPage() {
        return webPages.stream()
                .min((o1, o2) -> Integer.compare(o1.getUrl().length(), o2.getUrl().length()))
                .orElseGet(null);
    }
}
