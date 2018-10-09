package munch.gallery;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import munch.article.data.Article;
import munch.file.Image;
import munch.instagram.data.InstagramMedia;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 9/10/18
 * Time: 2:28 PM
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class PlaceImage {
    private String imageId;
    private List<Image.Size> sizes;
    private String sort;

    private Article article;
    private InstagramMedia instagramMedia;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public List<Image.Size> getSizes() {
        return sizes;
    }

    public void setSizes(List<Image.Size> sizes) {
        this.sizes = sizes;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
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

    @Override
    public String toString() {
        return "PlaceImage{" +
                "imageId='" + imageId + '\'' +
                ", sizes=" + sizes +
                ", sort='" + sort + '\'' +
                ", article=" + article +
                ", instagramMedia=" + instagramMedia +
                '}';
    }
}
