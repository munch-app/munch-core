package munch.api.services.places;

import com.google.common.collect.ImmutableSet;
import munch.data.Place;
import munch.data.places.PlaceBusinessHourCard;
import munch.data.places.PlaceImageBannerCard;
import munch.data.places.PlaceLocationCard;
import munch.data.places.PlaceNameTagCard;

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

    PlaceNameTagCard createName(Place place) {
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

    PlaceLocationCard createLocationDetail(Place place) {
        PlaceLocationCard card = new PlaceLocationCard();
        card.setLocation(place.getLocation());
        return card;
    }
}
