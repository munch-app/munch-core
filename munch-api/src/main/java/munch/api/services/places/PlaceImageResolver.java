package munch.api.services.places;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.ImageClient;
import munch.api.struct.Place;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 23/4/2017
 * Time: 11:32 AM
 * Project: munch-core
 */
@Singleton
public class PlaceImageResolver {

    private final ImageClient imageClient;

    @Inject
    public PlaceImageResolver(ImageClient imageClient) {
        this.imageClient = imageClient;
    }

    /**
     * @param places places to resolve
     */
    public void resolve(List<Place> places) {
        places.forEach(this::resolve);
    }

    public void resolve(Place place) {
        // TODO image to resolve
        // Delete
        // And resolve
    }
}
