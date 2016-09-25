package com.munch.core.struct.block.source.finals;

import java.util.HashMap;
import java.util.Map;

/**
 * Created By: Fuxing Loh
 * Date: 25/9/2016
 * Time: 4:36 PM
 * Project: struct
 */
public class FinalUrlSource {

    private String url;
    private double score;

    private Map<String, Float> tags = new HashMap<>();
    private Map<String, Float> scores = new HashMap<>();

    private int source;
    private String sourceUrl;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public Map<String, Float> getTags() {
        return tags;
    }

    public Map<String, Float> getScores() {
        return scores;
    }
}
