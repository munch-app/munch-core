package munch.catalyst;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mashape.unirest.http.Unirest;
import com.typesafe.config.ConfigFactory;
import munch.catalyst.clients.DataClient;
import munch.catalyst.clients.SearchClient;
import munch.data.*;

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
        System.setProperty("services.search.url", "");
        System.setProperty("services.data.url", "");

        requestInjection(this);
        Unirest.setTimeouts(60000, 60000);
    }

    @Provides
    DataClient providePlaceClient() {
        return new EmptyDataClient();
    }

    @Provides
    SearchClient provideSearchClient() {
        return new EmptySearchClient();
    }

    static class EmptyDataClient extends DataClient {
        public EmptyDataClient() {
            super(ConfigFactory.load());
        }

        @Override
        public void put(Place place, long cycleNo) {
        }

        @Override
        public void put(InstagramMedia media, long cycleNo) {
        }

        @Override
        public void put(Article article, long cycleNo) {
        }

        @Override
        public void deletePlaces(long cycleNo) {
        }

        @Override
        public void deleteArticles(long cycleNo) {
        }

        @Override
        public void deleteInstagramMedias(long cycleNo) {
        }
    }

    static class EmptySearchClient extends SearchClient {
        public EmptySearchClient() {
            super(ConfigFactory.load());
        }

        @Override
        public void put(Place place, long cycleNo) {
        }

        @Override
        public void put(Location location, long cycleNo) {
        }

        @Override
        public void deletePlaces(long cycleNo) {
        }

        @Override
        public void deleteLocations(long cycleNo) {
        }

        @Override
        public void put(Tag tag, long cycleNo) {
        }

        @Override
        public void deleteTags(long cycleNo) {
        }
    }
}
