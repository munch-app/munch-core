package munch.gallery;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import munch.file.Image;

import java.util.List;

/**
 * Ideally should migrate this to a single project
 * <p>
 * Created by: Fuxing
 * Date: 9/10/18
 * Time: 2:28 PM
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class PlaceImage {
    private String imageId;
    private String sort;
    private List<Image.Size> sizes;

    private String title;
    private String caption;

    private Article article;
    private Instagram instagram;
    private Long createdMillis;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
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

    public Instagram getInstagram() {
        return instagram;
    }

    public void setInstagram(Instagram instagram) {
        this.instagram = instagram;
    }

    public Long getCreatedMillis() {
        return createdMillis;
    }

    public void setCreatedMillis(Long createdMillis) {
        this.createdMillis = createdMillis;
    }

    @Override
    public String toString() {
        return "PlaceImage{" +
                "imageId='" + imageId + '\'' +
                ", sort='" + sort + '\'' +
                ", sizes=" + sizes +
                ", article=" + article +
                ", instagram=" + instagram +
                ", createdMillis=" + createdMillis +
                '}';
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Article {
        private String articleId;
        private String url;

        private String domainId;
        private munch.article.data.Article.Domain domain;

        public String getArticleId() {
            return articleId;
        }

        public void setArticleId(String articleId) {
            this.articleId = articleId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDomainId() {
            return domainId;
        }

        public void setDomainId(String domainId) {
            this.domainId = domainId;
        }

        public munch.article.data.Article.Domain getDomain() {
            return domain;
        }

        public void setDomain(munch.article.data.Article.Domain domain) {
            this.domain = domain;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Instagram {
        private String accountId;
        private String link;

        private String mediaId;
        private String username;

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getMediaId() {
            return mediaId;
        }

        public void setMediaId(String mediaId) {
            this.mediaId = mediaId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
