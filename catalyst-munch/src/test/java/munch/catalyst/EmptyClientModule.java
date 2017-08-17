package munch.catalyst;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mashape.unirest.http.Unirest;
import munch.catalyst.clients.DataClient;
import munch.catalyst.clients.SearchClient;
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
        Unirest.setTimeouts(60000, 60000);
    }

    @Provides
    DataClient providePlaceClient() {
        return new EmtpyDataClient();
    }

    @Provides
    SearchClient provideSearchClient() {
        return new EmptySearchClient();
    }

    static class EmtpyDataClient extends DataClient {
        public EmtpyDataClient() {
            super("");
        }

        @Override
        public void put(Place place) {
        }

        @Override
        public void deleteBefore(Date updatedDate) {
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
