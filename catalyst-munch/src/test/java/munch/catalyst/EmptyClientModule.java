package munch.catalyst;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mashape.unirest.http.Unirest;
import munch.catalyst.clients.ArticleClient;
import munch.catalyst.clients.InstagramClient;
import munch.catalyst.clients.PlaceClient;
import munch.catalyst.clients.SearchClient;
import munch.catalyst.data.Article;
import munch.catalyst.data.InstagramMedia;
import munch.catalyst.data.Location;
import munch.catalyst.data.Place;

import java.util.Date;

/**
 * In theses clients: catalystId is also know also placeId
 * <p>
 * Created by: Fuxing
 * Date: 15/4/2017
 * Time: 3:44 AM
 * Project: munch-core
 */
public class EmptyClientModule extends AbstractModule {

    @Override
    protected void configure() {
        requestInjection(this);
        Unirest.setTimeouts(60000, 600000);
    }

    @Provides
    ArticleClient provideArticleClient() {
        return new EmtpyArticleClient();
    }

    @Provides
    PlaceClient providePlaceClient() {
        return new EmtpyPlaceClient();
    }

    @Provides
    InstagramClient provideInstagramClient() {
        return new EmtpyInstagramClient();
    }

    @Provides
    SearchClient provideSearchClient() {
        return new EmptySearchClient();
    }

    static class EmtpyArticleClient extends ArticleClient {
        public EmtpyArticleClient() {
            super("");
        }

        @Override
        public Article put(Article article) {
            return article;
        }

        @Override
        public void deleteBefore(String catalystId, Date updatedDate) {
        }
    }

    static class EmtpyPlaceClient extends PlaceClient {
        public EmtpyPlaceClient() {
            super("");
        }

        @Override
        public void put(Place place) {
        }

        @Override
        public void deleteBefore(Date updatedDate) {
        }
    }

    static class EmtpyInstagramClient extends InstagramClient {
        public EmtpyInstagramClient() {
            super("");
        }

        @Override
        public InstagramMedia put(InstagramMedia media) {
            return media;
        }

        @Override
        public void deleteBefore(String catalystId, Date updatedDate) {
        }
    }

    static class EmptySearchClient extends SearchClient {
        public EmptySearchClient() {
            super("");
        }

        @Override
        public void put(Place place) {
        }

        @Override
        public void put(Location location) {
        }

        @Override
        public void deleteBefore(String type, Date updatedDate) {
        }
    }
}
