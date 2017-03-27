package munch.api;

import com.google.common.collect.ImmutableList;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import munch.struct.*;
import org.apache.commons.lang3.RandomUtils;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created By: Fuxing Loh
 * Date: 27/3/2017
 * Time: 5:06 PM
 * Project: munch-core
 */
@Singleton
public class PlaceRandom {

    private final Lorem lorem = LoremIpsum.getInstance();
    private final List<String> establishments = ImmutableList.of(
            "Restaurant", "Bars & Pubs", "Cafe",
            "Buffet", "BBQ", "Hawker", "Steamboat", "Desserts");
    private final List<String> amenities = ImmutableList.of(
            "Halal", "Healthy", "Vegetarian", "Pet Friendly");

    /**
     * Override random data for testing phase
     *
     * @param place place, add random data to it
     */
    public void random(Place place) {
        place.setPrice(randomPrice());
        place.setPhone("+65 9999 4544");
        place.setWebsite("www.munchapp.co");
        place.setDescription(lorem.getParagraphs(2, 5));

        place.setEstablishments(Collections.singleton(establishments.get(RandomUtils.nextInt(0, establishments.size()))));
        place.setAmenities(Collections.singleton(amenities.get(RandomUtils.nextInt(0, amenities.size()))));

        place.setMedias(randomMedias());
        place.setMenus(randomMenus());
        place.setHours(randomHours());
    }

    private Price randomPrice() {
        Price price = new Price();
        price.setLowest(RandomUtils.nextDouble(5, 20));
        price.setHighest(RandomUtils.nextDouble(20, 40));
        return price;
    }

    private Set<Hour> randomHours() {
        Set<Hour> hours = new HashSet<>();
        for (int i = 1; i < 8; i++) {
            Hour hour = new Hour();
            hour.setDay(i);
            hour.setOpen("11:00");
            hour.setClose("22:00");
            hours.add(hour);
        }
        return hours;
    }

    private Set<Menu> randomMenus() {
        Set<Menu> menus = new HashSet<>();
        Menu menu = new Menu();
        menu.setType(Menu.TYPE_PDF);
        menu.setUrl("http://nandos.com.sg/nandos_sg/download/Nandos-Singapore-Website-Menu-Jan-2017.pdf");
        menus.add(menu);
        return menus;
    }

    private Set<Media> randomMedias() {
        Set<Media> medias = new HashSet<>();
        Media media = new Media();
        media.setType(Media.TYPE_IMAGE);
        media.setView(Media.VIEW_BANNER);
        media.setUrl("http://nandos.com.sg/nandos_sg/images/restaurants/junction8.png");
        medias.add(media);
        return medias;
    }
}
