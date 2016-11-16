package com.munch.core.struct.rdbms.place;

import com.munch.core.essential.source.MunchSource;
import com.munch.core.struct.rdbms.locality.AbstractCountryTest;
import com.munch.core.struct.rdbms.locality.Location;
import com.munch.core.struct.rdbms.locality.LocationTestInterface;
import com.munch.core.struct.util.HibernateUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by: Fuxing
 * Date: 12/11/2016
 * Time: 11:43 PM
 * Project: munch-core
 */
public class PlaceTest extends AbstractCountryTest implements PlaceTestInterface, LocationTestInterface {

    protected String placeId;

    @BeforeEach
    public void setUp() throws Exception {
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (placeId != null) {
            removeEntity(Place.class, placeId);
        }
    }

    @Test
    public void notNull() throws Exception {
        placeId = HibernateUtil.reduce(em -> {
            Place place = new Place();
            place.setName("Place Name");

            // PlaceLink required but all can be null
            place.setPlaceLink(new PlaceLink());

            // Location Not Null
            Location location = new Location();
            location.setLat(1.38);
            location.setLng(138.3);
            location.setAddress("73 Ayer Rajah Crescent #03-27 139952");
            location.setCountry(getCountry(em));
            place.setLocation(location);

            // PlaceLog Not Null
            PlaceLog log = new PlaceLog();
            log.setAddedHow(MunchSource.MUNCH_STAFF);
            log.setAddedThrough(PlaceLog.THROUGH_CORPUS_BLOG);
            log.setAddedBy("fuxing");
            place.setPlaceLog(log);

            em.persist(place);
            return place.getId();
        });
    }

    @Test
    public void manyToOne() throws Exception {
        placeId = HibernateUtil.reduce(em -> {
            Place place = new Place();
            place.setName("Place Name");

            place.setPlaceLink(new PlaceLink());
            place.setLocation(createDefault(em));
            place.setPlaceLog(createLogDefault());

            place.getPlaceHours().add(createHour(PlaceHour.MON, "10:00", "20:00"));
            place.getPlaceHours().add(createHour(PlaceHour.TUE, "10:00", "20:00"));

            em.persist(place);
            return place.getId();
        });

        HibernateUtil.with(em -> {
            Place place = em.find(Place.class, placeId);
            assertThat(place.getPlaceHours()).hasSize(2);

            place.getPlaceHours().add(createHour(PlaceHour.WED, "10:00", "20:00"));
            place.getPlaceHours().add(createHour(PlaceHour.THU, "10:00", "20:00"));
            place.getPlaceHours().add(createHour(PlaceHour.FRI, "10:00", "22:00"));
            assertThat(place.getPlaceHours()).hasSize(5);
            em.persist(place);
        });

        HibernateUtil.with(em -> {
            Place place = em.find(Place.class, placeId);
            assertThat(place.getPlaceHours()).hasSize(5);
            place.getPlaceHours().removeIf(h -> h.getDay() == PlaceHour.MON);
            assertThat(place.getPlaceHours()).hasSize(4);
        });

        HibernateUtil.with(em -> {
            Place place = em.find(Place.class, placeId);
            assertThat(place.getPlaceHours()).hasSize(4);
        });
    }

}