package com.munch.utils.spark;

import org.junit.jupiter.api.Test;

/**
 * Created By: Fuxing Loh
 * Date: 16/2/2017
 * Time: 6:09 PM
 * Project: munch-core
 */
class SparkServerTest {

    @Test
    void getClassName() throws Exception {
        java.text.NumberFormat format = java.text.NumberFormat.getInstance();
        System.out.println(format.getClass().getSimpleName());
    }
}