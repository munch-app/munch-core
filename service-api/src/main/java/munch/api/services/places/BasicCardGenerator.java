package munch.api.services.places;

import munch.data.Place;
import munch.data.places.ImageBannerCard;
import munch.data.places.NameCard;
import munch.data.places.PlaceCard;

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

        ImageBannerCard imageBanner = createImageBanner(place);
        if (imageBanner != null) cards.add(imageBanner);

        cards.add(createName(place));
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
}
