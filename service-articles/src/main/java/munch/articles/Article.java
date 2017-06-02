package munch.articles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import munch.articles.hibernate.PojoUserType;
import munch.clients.ImageMeta;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 2/4/2017
 * Time: 5:04 PM
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public final class Article {
    private String placeId;
    private String articleId;

    private String brand;
    private String url;

    private String title;
    private String summary;
    private Set<ArticleImage> images;

    private Date updatedDate;

    /**
     * @return place that is linked to the article
     */
    @Column(columnDefinition = "CHAR(36)", nullable = false, updatable = false)
    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    /**
     * @return unique articleId of article in munch
     */
    @Id
    @Column(nullable = false, length = 2048)
    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    @Column(nullable = false, length = 255)
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Column(nullable = false, length = 2048)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(nullable = false, length = 255)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(nullable = false, length = 1000)
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "article")
    public Set<ArticleImage> getImages() {
        return images;
    }

    public void setImages(Set<ArticleImage> images) {
        this.images = images;
    }

    @Column(nullable = false)
    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * User type for Article entity
     */
    static class UserType {
        static class ImageUrls extends PojoUserType<String[]> {
            ImageUrls() {
                super(String[].class);
            }
        }

        static class Images extends PojoUserType<ImageMeta[]> {
            Images() {
                super(ImageMeta[].class);
            }
        }
    }
}
