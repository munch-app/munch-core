package com.instagram.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Set;

/**
 * Date: 3/10/19
 * Time: 7:53 pm
 *
 * @author Fuxing Loh
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class InstagramMedia {

    @JsonProperty("id")
    private String id;

    /**
     * 'image', 'video'
     */
    @JsonProperty("type")
    private String type;

    @JsonProperty("link")
    private String link;

    @JsonProperty("likes")
    private InstagramCount likes;

    @JsonProperty("comments")
    private InstagramCount comments;

    @JsonProperty("caption")
    private InstagramCaption caption;

    @JsonProperty("user")
    private InstagramUser user;

    @JsonProperty("created_time")
    private Integer createdTime;

    @JsonProperty("filter")
    private String filter;

    @JsonProperty("tags")
    private Set<String> tags;

    @JsonProperty("images")
    private Map<String, InstagramFile> images;

    @JsonProperty("videos")
    private Map<String, InstagramFile> videos;

    @JsonProperty("location")
    private InstagramLocation location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public InstagramCount getLikes() {
        return likes;
    }

    public void setLikes(InstagramCount likes) {
        this.likes = likes;
    }

    public InstagramCount getComments() {
        return comments;
    }

    public void setComments(InstagramCount comments) {
        this.comments = comments;
    }

    public InstagramCaption getCaption() {
        return caption;
    }

    public void setCaption(InstagramCaption caption) {
        this.caption = caption;
    }

    public InstagramUser getUser() {
        return user;
    }

    public void setUser(InstagramUser user) {
        this.user = user;
    }

    public Integer getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Integer createdTime) {
        this.createdTime = createdTime;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Map<String, InstagramFile> getImages() {
        return images;
    }

    public void setImages(Map<String, InstagramFile> images) {
        this.images = images;
    }

    public Map<String, InstagramFile> getVideos() {
        return videos;
    }

    public void setVideos(Map<String, InstagramFile> videos) {
        this.videos = videos;
    }

    public InstagramLocation getLocation() {
        return location;
    }

    public void setLocation(InstagramLocation location) {
        this.location = location;
    }
}
