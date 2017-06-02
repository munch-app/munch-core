package munch.places.data.hibernate;


import munch.places.data.Place;

/**
 * Created By: Fuxing Loh
 * Date: 10/3/2017
 * Time: 2:30 PM
 * Project: munch-core
 */
public class PriceUserType extends PojoUserType<Place.Price> {

    public PriceUserType() {
        super(Place.Price.class);
    }

}