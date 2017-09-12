package munch.api.services.places;

import com.google.common.collect.ImmutableSet;
import munch.data.Place;
import munch.data.places.BusinessHourCard;
import munch.data.places.ImageBannerCard;
import munch.data.places.LocationCard;
import munch.data.places.NameTagCard;

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
    ImageBannerCard createImageBanner(Place place) {
        List<Place.Image> images = place.getImages();
        if (images.isEmpty()) return null;

        ImageBannerCard card = new ImageBannerCard();
        card.setImages(images);
        return card;
    }

    NameTagCard createName(Place place) {
        NameTagCard card = new NameTagCard();
        card.setName(place.getName());
        card.setTags(ImmutableSet.copyOf(place.getTags()));
        return card;
    }

    @Nullable
    BusinessHourCard createBusinessHour(Place place) {
        if (place.getHours() == null) return null;
        if (place.getHours().isEmpty()) return null;

        BusinessHourCard card = new BusinessHourCard();
        card.setHours(place.getHours());
        return card;
    }

    LocationCard createLocationDetail(Place place) {
        LocationCard card = new LocationCard();
        card.setLocation(place.getLocation());
        return card;
    }
}
