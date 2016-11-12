package com.munch.core.struct.rdbms.locality;

import javax.persistence.EntityManager;

import static com.munch.core.struct.rdbms.locality.AbstractCountryTest.getCountry;

/**
 * Created by: Fuxing
 * Date: 13/11/2016
 * Time: 12:23 AM
 * Project: munch-core
 */
public interface LocationTestInterface {

    default Location create(EntityManager em, double lat, double lng, String address) {
        Location location = new Location();
        location.setLat(lat);
        location.setLng(lng);
        location.setAddress(address);
        location.setCountry(getCountry(em));
        return location;
    }

    default Location createDefault(EntityManager em) {
        return create(em, 1.38, 108.3, "73 Ayer Rajah Crescent #03-27 139952");
    }

}
