package munch.catalyst.clients;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 2/4/2017
 * Time: 5:04 PM
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Article {
    private String placeId;
    private String articleId;

    private String brand;
    private String url;

    private String title;
    private String description;
    private List<ArticleImage> images;

    private Date createdDate;
    private Date updatedDate;

    /**
     * @return place that is linked to the article
     */
    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    /**
     * @return unique articleId of article in munch
     */
    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public void setDescription(String summary) {
        this.description = summary;
    }

    public List<ArticleImage> getImages() {
        return images;
    }

    public void setImages(List<ArticleImage> images) {
        this.images = images;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * This extends ImageMeta, but not at catalyst
     * Article version requires url to be saved
     */
    public static final class ArticleImage {
        private String url;

        public ArticleImage() {
        }

        public ArticleImage(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
