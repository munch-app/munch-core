package munch.places;

import munch.restful.server.exceptions.StructuredException;

/**
 * Created by: Fuxing
 * Date: 23/4/2017
 * Time: 4:42 PM
 * Project: munch-core
 */
public class PlaceNotFound extends StructuredException {

    public PlaceNotFound(String placeId) {
        super("PlaceNotFoundException", "Place id = " + placeId + " cannot be found in the database.", 400);
    }
}
