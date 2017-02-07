package com.munch.accounts.objects;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created By: Fuxing Loh
 * Date: 7/2/2017
 * Time: 7:57 PM
 * Project: munch-core
 */
public enum Gender {
    Male("M"),
    Female("F");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    /**
     * 1 Letter representation of enum Gender
     *
     * @return value of Gender which is also json value
     */
    @JsonValue
    public String getValue() {
        return value;
    }
}
