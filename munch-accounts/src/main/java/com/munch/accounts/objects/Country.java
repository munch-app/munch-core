package com.munch.accounts.objects;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by: Fuxing
 * Date: 7/2/2017
 * Time: 2:30 AM
 * Project: munch-core
 */
public enum Country {
    Singapore("SGP", "Singapore", 702),
    Malaysia("MYS", "Malaysia", 458);

    private final String iso;
    private final String name;
    private final int code;

    Country(String iso, String name, int code) {
        this.iso = iso;
        this.name = name;
        this.code = code;
    }

    /**
     * @return 3 char iso of the Country
     */
    @JsonValue
    public String getIso() {
        return iso;
    }

    /**
     * @return name of the Country
     */
    public String getName() {
        return name;
    }

    /**
     * @return int code of the country
     */
    public int getCode() {
        return code;
    }
}
