package munch.articles;

import java.util.Date;
import java.util.Set;

/**
 * Created By: Fuxing Loh
 * Date: 2/6/2017
 * Time: 12:39 PM
 * Project: munch-core
 */
public final class PutRequest {
    private String placeId;
    private String articleId;

    private String brand;
    private String url;

    private String title;
    private String summary;
    private Set<String> imageUrls;

    private Date updatedDate;

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Set<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(Set<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }


    /**
     * Convert put request to article
     *
     * @param request request
     * @return Article
     */
    public static Article convert(PutRequest request) {
        Article article = new Article();
        article.setPlaceId(request.placeId);
        article.setArticleId(request.articleId);

        article.setBrand(request.brand);
        article.setUrl(request.url);
        article.setTitle(request.title);
        article.setSummary(request.summary);

        article.setImages(new ImageSet());
        article.setUpdatedDate(request.updatedDate);
        return article;
    }

    /**
     * Merge put request into existing article
     * ImageSet is not merged in TODO
     *
     * @param article article
     * @param request request
     * @return Article
     */
    public static void merge(Article article, PutRequest request) {
        article.setPlaceId(request.placeId);
        article.setArticleId(request.articleId);

        article.setBrand(request.brand);
        article.setUrl(request.url);
        article.setTitle(request.title);
        article.setSummary(request.summary);

        article.setUpdatedDate(request.updatedDate);
    }
}
