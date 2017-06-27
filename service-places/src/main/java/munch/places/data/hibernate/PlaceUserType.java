package munch.places.data.hibernate;

import munch.places.data.Place;

/**
 * Created by: Fuxing
 * Date: 27/6/2017
 * Time: 4:56 PM
 * Project: munch-core
 */
public final class PlaceUserType {
    public final static class Hours extends PojoUserType<Place.Hour[]> {
        public Hours() {
            super(Place.Hour[].class);
        }
    }

    public final static class Location extends PojoUserType<Place.Location> {
        public Location() {
            super(Place.Location.class);
        }
    }

    public final static class Price extends PojoUserType<Place.Price> {
        public Price() {
            super(Place.Price.class);
        }
    }

    public final static class Tags extends PojoUserType<String[]> {
        public Tags() {
            super(String[].class);
        }
    }

    public final static class Images extends PojoUserType<Place.Image[]> {
        public Images() {
            super(Place.Image[].class);
        }
    }
}
