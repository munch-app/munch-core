package munch.search.place;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import munch.search.ElasticModule;

import java.io.IOException;

/**
 * Created By: Fuxing Loh
 * Date: 17/4/2017
 * Time: 5:50 PM
 * Project: munch-core
 */
public class PlaceModule extends AbstractModule {

    @Override
    protected void configure() {
        requestInjection(this);
    }

    @Inject
    void configureMapping(PlaceMapping mapping) throws IOException {
        PlaceMapping.Result result = mapping.validate();
        switch (result) {
            case Success:
                return;
            case NoIndexError:
                mapping.createIndex();
                if (mapping.validate() != PlaceMapping.Result.Success) {
                    throw new RuntimeException("Place created index error.");
                }
                return;
            case UnknownError:
                throw new RuntimeException("Place unknown index error.");
            case GeoIndexError:
                throw new RuntimeException("Place geo index error.");
        }
    }
}
