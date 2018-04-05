package munch.api.services.places.partner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import munch.article.clients.Article;
import munch.corpus.instagram.InstagramMedia;

import java.util.Date;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 5/4/18
 * Time: 3:49 PM
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class PartnerContent {
    private String uniqueId;
    private String type;
    private Map<String, String> image;

    private Date date;
    private String author;
    private String title;
    private String description;

    private Article article;
    private InstagramMedia instagramMedia;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Map<String, String> getImage() {
        return image;
    }

    public void setImage(Map<String, String> image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public InstagramMedia getInstagramMedia() {
        return instagramMedia;
    }

    public void setInstagramMedia(InstagramMedia instagramMedia) {
        this.instagramMedia = instagramMedia;
    }
}
