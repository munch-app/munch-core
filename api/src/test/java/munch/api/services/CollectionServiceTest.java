package munch.api.services;

import munch.restful.core.JsonUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 16/1/18
 * Time: 9:40 PM
 * Project: munch-core
 */
class CollectionServiceTest {

    @Test
    void date() {
        System.out.println(JsonUtils.toString(new Date()));
    }
}