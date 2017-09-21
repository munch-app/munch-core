package munch.api.services.places;

import com.google.common.collect.ImmutableSet;
import munch.api.services.places.cards.PlaceBusinessHourCard;
import munch.api.services.places.cards.PlaceImageBannerCard;
import munch.api.services.places.cards.PlaceLocationCard;
import munch.api.services.places.cards.PlaceNameTagCard;
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

    PlaceLocationCard createLocationDetail(Place place) {
        PlaceLocationCard card = new PlaceLocationCard();
        card.setName(place.getName());
        card.setLocation(place.getLocation());
        return card;
    }
}
