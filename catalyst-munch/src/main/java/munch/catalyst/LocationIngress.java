package munch.catalyst;

import corpus.data.CorpusData;
import munch.catalyst.builder.LocationBuilder;
import munch.catalyst.clients.SearchClient;
import munch.data.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 20/8/2017
 * Time: 5:09 AM
 * Project: munch-core
 */
@Singleton
public final class LocationIngress extends AbstractIngress {
    private static final Logger logger = LoggerFactory.getLogger(LocationIngress.class);

    private final SearchClient searchClient;

    @Inject
    public LocationIngress(SearchClient searchClient) {
        this.searchClient = searchClient;
    }

    @Override
    protected void egress(long cycleNo) {
        searchClient.deleteLocations(cycleNo);
    }

    /**
     * Validate a collection of links to make sure it can be a munch place
     * Validated with conditions:
     * 1. NEA Licence
     * 2. Blogger mentions
     *
     * @param dataList links
     * @return true = validated, false = cannot be a munch place
     */
    protected boolean validate(List<CorpusData> dataList) {
        // Must have corpus: LocationPolygon
        return hasCorpusName(dataList, "Sg.Munch.LocationPolygon");
    }

    protected void put(List<CorpusData> dataList, final long cycleNo) {
        // Else validate = success: put new place
        LocationBuilder locationBuilder = new LocationBuilder();
        dataList.forEach(locationBuilder::consume);

        // Collect locations
        Date updatedDate = new Timestamp(System.currentTimeMillis());
        for (Location location : locationBuilder.collect(updatedDate)) {
            logger.info("Putting location id: {} name: {}", location.getId(), location.getName());
            searchClient.put(location, cycleNo);
        }
    }
}
