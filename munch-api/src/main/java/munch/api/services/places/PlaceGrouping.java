package munch.api.services.places;

import com.google.inject.Singleton;
import munch.api.struct.Place;
import munch.api.struct.PlaceCollection;

import java.util.Collection;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 23/4/2017
 * Time: 9:47 AM
 * Project: munch-core
 */
@Singleton
public class PlaceGrouping {

    /**
     * @param places    places to parse to group
     * @param groupSize average group size
     * @param minGroups min number of group
     * @param maxGroups max number of group
     * @return List of PlaceCollection (Groups)
     */
    public List<PlaceCollection> parse(Collection<Place> places, int groupSize, int minGroups, int maxGroups) {
        // TODO parse
        return null;
    }

}
