package munch.api.data;

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
public class PlaceDetail {
    private Place place;
    private List<Media> medias;
    private List<Article> articles;
    // Future: reviews

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public List<Media> getMedias() {
        return medias;
    }

    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
