package munch.api.services.places;

import com.google.common.collect.ImmutableSet;
import munch.api.services.places.cards.*;
import munch.data.Place;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 6/9/2017
 * Time: 12:54 AM
 * Project: munch-core
 */
@Singleton
public final class BasicCardGenerator {

    @Nullable
    PlaceImageBannerCard createImageBanner(Place place) {
        List<Place.Image> images = place.getImages();
        if (images.isEmpty()) return null;

        PlaceImageBannerCard card = new PlaceImageBannerCard();
        card.setImages(images);
        return card;
    }

    PlaceNameTagCard createNameTag(Place place) {
        PlaceNameTagCard card = new PlaceNameTagCard();
        card.setName(place.getName());
        card.setTags(ImmutableSet.copyOf(place.getTags()));
        return card;
    }

    @Nullable
    PlaceBusinessHourCard createBusinessHour(Place place) {
        if (place.getHours() == null) return null;
        if (place.getHours().isEmpty()) return null;

        PlaceBusinessHourCard card = new PlaceBusinessHourCard();
        card.setHours(place.getHours());
        return card;
    }

    PlaceAddressCard createAddress(Place place) {
        PlaceAddressCard card = new PlaceAddressCard();
        card.setAddress(place.getLocation().getAddress());

        card.setStreet(place.getLocation().getStreet());
        card.setUnitNumber(place.getLocation().getUnitNumber());
        card.setCity(place.getLocation().getCity());
        card.setCountry(place.getLocation().getCountry());
        card.setPostal(place.getLocation().getPostal());

        card.setLatLng(place.getLocation().getLatLng());
        card.setNearestTrain(place.getLocation().getNearestTrain());
        return card;
    }

    PlaceLocationCard createLocation(Place place) {
        PlaceLocationCard card = new PlaceLocationCard();
        card.setPlaceName(place.getName());
        card.setLatLng(place.getLocation().getLatLng());
        return card;
    }
}
