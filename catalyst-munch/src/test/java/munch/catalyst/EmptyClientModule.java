package munch.catalyst;

import catalyst.data.CorpusData;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mashape.unirest.http.Unirest;
import munch.catalyst.clients.ArticleClient;
import munch.catalyst.clients.MediaClient;
import munch.catalyst.clients.PlaceClient;
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
    MediaClient provideMediaClient() {
        return new EmtpyMediaClient();
    }

    static class EmtpyArticleClient extends ArticleClient {
        public EmtpyArticleClient() {
            super("");
        }

        @Override
        public void put(CorpusData data, Date updatedDate) {
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

    static class EmtpyMediaClient extends MediaClient {
        public EmtpyMediaClient() {
            super("");
        }

        @Override
        public void put(CorpusData data, Date updatedDate) {
        }

        @Override
        public void deleteBefore(String catalystId, Date updatedDate) {
        }
    }
}
