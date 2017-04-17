package munch.catalyst;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import munch.struct.Place;

import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 15/3/2017
 * Time: 6:08 PM
 * Project: munch-core
 */
public class NoSearchModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SearchStore.class).to(EmptySearchStore.class);
    }

    @Singleton
    private static class EmptySearchStore implements SearchStore {
        @Override
        public void put(Place place) throws Exception {

        }

        @Override
        public void put(List<Place> places) throws Exception {

        }

        @Override
        public void delete(String key) throws Exception {

        }

        @Override
        public void delete(List<String> keys) throws Exception {

        }
    }
}
