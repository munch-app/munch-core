package com.munch.core.struct.rdbms.locality;

/**
 * Created By: Fuxing Loh
 * Date: 29/9/2016
 * Time: 7:44 PM
 * Project: struct
 */
public class CountryTest {

    public static Country create(long id) {
        Country country = new Country();
        country.setId(id);
        return country;
    }

}