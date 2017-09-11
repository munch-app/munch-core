package munch.api.services.places;

import com.google.common.collect.ImmutableSet;
import munch.data.Place;
import munch.data.places.*;

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

    NameCard createName(Place place) {
        NameCard card = new NameCard();
        card.setName(place.getName());
        return card;
    }

    TagCard createTag(Place place) {
        TagCard card = new TagCard();
        if (place.getTags() != null) {
            card.setTags(ImmutableSet.copyOf(place.getTags()));
        } else {
            card.setTags(ImmutableSet.of());
        }
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

    LocationDetailCard createLocationDetail(Place place) {
        LocationDetailCard card = new LocationDetailCard();
        card.setLocation(place.getLocation());
        return card;
    }

    LocationMapCard createLocationMap(Place place) {
        // Safety checks
        if (place.getLocation() == null) return null;
        if (place.getLocation().getLatLng() == null) return null;

        LocationMapCard card = new LocationMapCard();
        card.setLocation(place.getLocation());
        return card;
    }
}
