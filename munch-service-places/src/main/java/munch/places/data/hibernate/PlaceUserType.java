package munch.places.data.hibernate;

import munch.struct.places.Place;

/**
 * Created By: Fuxing Loh
 * Date: 10/3/2017
 * Time: 2:30 PM
 * Project: munch-core
 */
public class PlaceUserType extends PojoUserType<Place> {

    public PlaceUserType() {
        super(Place.class);
    }

}
