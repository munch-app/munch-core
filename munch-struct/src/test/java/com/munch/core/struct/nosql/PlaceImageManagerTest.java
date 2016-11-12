package com.munch.core.struct.nosql;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

/**
 * Created By: Fuxing Loh
 * Date: 26/9/2016
 * Time: 11:16 PM
 * Project: struct
 */
public class PlaceImageManagerTest {
    @Test
    public void timeNow() throws Exception {
        for (int i = 0; i < 100; i++) {
            System.out.println(System.currentTimeMillis());
        }

    }

    @Test
    public void anyBlank() throws Exception {
        System.out.println(StringUtils.isAnyBlank(null, null));
        System.out.println(StringUtils.isNoneBlank("123", "fr"));

        System.out.println(StringUtils.isAnyBlank("123", "4565"));
        System.out.println(StringUtils.isNoneBlank("", ""));
    }
}