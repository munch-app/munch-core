package munch.api.services.places;

import com.google.common.collect.ImmutableSet;
import munch.data.Place;
import munch.data.places.*;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 6/9/2017
 * Time: 12:54 AM
 * Project: munch-core
 */
@Singleton
public final class BasicCardGenerator {

    /**
     * @param place place
     * @return All generated Basic typed Card
     */
    List<PlaceCard> generate(Place place) {
        List<PlaceCard> cards = new ArrayList<>();

        cards.add(createImageBanner(place));
        cards.add(createName(place));
        cards.add(createTag(place));
        cards.add(createImageBanner(place));
        cards.add(createLocationDetail(place));

        return cards;
    }

    @Nullable
    private ImageBannerCard createImageBanner(Place place) {
        List<Place.Image> images = place.getImages();
        if (images.isEmpty()) return null;

        ImageBannerCard card = new ImageBannerCard();
        card.setImage(images.get(0).getImageMeta());
        return card;
    }

    private NameCard createName(Place place) {
        NameCard card = new NameCard();
        card.setName(place.getName());
        return card;
    }

    private TagCard createTag(Place place) {
        TagCard card = new TagCard();
        if (place.getTags() != null) {
            card.setTags(ImmutableSet.copyOf(place.getTags()));
        } else {
            card.setTags(ImmutableSet.of());
        }
        return card;
    }

    @Nullable
    private BusinessHourCard createBusinessHour(Place place) {
        BusinessHourCard card = new BusinessHourCard();
        card.setHours(place.getHours());
        return null;
    }

    private LocationDetailCard createLocationDetail(Place place) {
        LocationDetailCard card = new LocationDetailCard();
        card.setLocation(place.getLocation());
        return card;
    }
}
