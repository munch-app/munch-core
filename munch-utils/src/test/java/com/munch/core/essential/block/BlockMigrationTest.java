package com.munch.core.essential.block;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by: Fuxing
 * Date: 17/10/2016
 * Time: 8:57 AM
 * Project: essential
 */
public class BlockMigrationTest {

    private Gson gson = new Gson();
    private JsonParser jsonParser = new JsonParser();

    @Test
    public void migration() throws Exception {
        JsonElement element = jsonParser.parse("{\"name\":\"real\"}");
        String name = element.getAsJsonObject().get("name").getAsString();
        assertThat(name).isEqualTo("real");
    }

}