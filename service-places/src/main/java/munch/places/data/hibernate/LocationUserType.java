package munch.places.data.hibernate;


import munch.places.data.Place;

/**
 * Created By: Fuxing Loh
 * Date: 10/3/2017
 * Time: 2:30 PM
 * Project: munch-core
 */
public class LocationUserType extends PojoUserType<Place.Location> {

    public LocationUserType() {
        super(Place.Location.class);
    }

}
