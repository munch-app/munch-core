package munch.search.place;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created By: Fuxing Loh
 * Date: 17/4/2017
 * Time: 5:50 PM
 * Project: munch-core
 */
public class PlaceModule extends AbstractModule {
    private static final Logger logger = LoggerFactory.getLogger(PlaceModule.class);

    @Override
    protected void configure() {
        requestInjection(this);
    }

    @Inject
    void configureMapping(PlaceMapping mapping) throws IOException {
        PlaceMapping.Result result = mapping.validate();
        logger.info("Place mapping validation result: {}", result.name());
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
