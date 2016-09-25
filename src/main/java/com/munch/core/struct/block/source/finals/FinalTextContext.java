package com.munch.core.struct.block.source.finals;

/**
 * String, Source, Context, Percent
 * Created By: Fuxing Loh
 * Date: 25/9/2016
 * Time: 4:35 PM
 * Project: struct
 */
public class FinalTextContext {

    private String text;

    // Left + text + Right = source of text with context
    private String left;
    private String right;

    // Percent from 0 to 1.0
    private double score;
    private long count;

    private int source;
    private String sourceUrl;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
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

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
