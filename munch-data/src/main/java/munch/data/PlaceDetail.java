package munch.data;

import java.util.List;

/**
 * Detail of Place for presentation to user
 * Includes medias, articles and reviews
 * DataNode that is to be sent to Munch App
 * <p>
 * Created By: Fuxing Loh
 * Date: 7/6/2017
 * Time: 4:48 PM
 * Project: munch-core
 */
public final class PlaceDetail {
    private Place place;
    private Instagram instagram;
    private List<Article> articles;
    // Future: reviews

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Instagram getInstagram() {
        return instagram;
    }

    public void setInstagram(Instagram instagram) {
        this.instagram = instagram;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public static class Instagram {
        private List<InstagramMedia> medias;

        public List<InstagramMedia> getMedias() {
            return medias;
        }

        public void setMedias(List<InstagramMedia> medias) {
            this.medias = medias;
        }
    }
}
