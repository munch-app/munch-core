package com.munch.core.struct.nosql;

import org.junit.Test;

/**
 * Created By: Fuxing Loh
 * Date: 23/9/2016
 * Time: 4:03 PM
 * Project: struct
 */
public class HashTest {

    @Test
    public void generateHash() throws Exception {
        System.out.println("2147483587".hashCode());
        System.out.println("2147483647".hashCode());
    }
}
