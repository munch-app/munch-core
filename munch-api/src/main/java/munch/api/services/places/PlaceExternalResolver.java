package munch.api.services.places;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.ImageClient;
import munch.api.struct.Image;
import munch.api.struct.Place;
import munch.restful.server.JsonCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Resolve external resources like image
 * <p>
 * Created by: Fuxing
 * Date: 23/4/2017
 * Time: 11:32 AM
 * Project: munch-core
 */
@Singleton
public class PlaceExternalResolver {
    private static final Logger logger = LoggerFactory.getLogger(PlaceExternalResolver.class);

    private final ImageClient imageClient;

    /**
     * @param imageClient image is a external resource that can be resolved
     */
    @Inject
    public PlaceExternalResolver(ImageClient imageClient) {
        this.imageClient = imageClient;
    }

    /**
     * Query string acceptable:
     * image.kinds = ""
     *
     * @param places places to resolve external resources
     * @param call   json call for additional data from query string
     */
    public void resolve(Collection<Place> places, JsonCall call) {
        resolveImage(places, call.queryString("image.kinds", ""));
    }

    /**
     * Query string acceptable:
     * image.kinds = ""
     *
     * @param place single place to resolve external resources
     * @param call  json call for additional data from query string
     */
    public void resolve(Place place, JsonCall call) {
        if (place == null) return;
        resolveImage(Collections.singleton(place), call.queryString("image.kinds", ""));
    }

    /**
     * @param places places to resolve
     */
    private void resolveImage(Collection<Place> places, String kinds) {
        // Map all to ImageKey : PlaceId
        Map<String, Place> imagePlaceMap = new HashMap<>();
        for (Place place : places) {
            for (Place.LinkedImage image : place.getLinkedImages()) {
                imagePlaceMap.put(image.getImageKey(), place);
            }
        }
        // Replace .linkedImages and .images
        places.forEach(place -> place.setImages(new ArrayList<>()));
        places.forEach(place -> place.setLinkedImages(null));

        Collection<String> keys = imagePlaceMap.keySet();
        if (keys.isEmpty()) return;

        // Add all images to place
        for (Image image : imageClient.batch(keys, kinds)) {
            Place place = imagePlaceMap.get(image.getKey());
            if (place != null) {
                place.getImages().add(image);
            } else {
                logger.warn("Image {} for Place not found.", image.getKey());
            }
        }
    }
}
