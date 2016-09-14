package com.munch.core.struct.block;

import com.google.gson.Gson;
import com.munch.core.struct.block.website.SourceWebsite;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by: Fuxing
 * Date: 15/9/2016
 * Time: 2:30 AM
 * Project: struct
 */
public class BlockVersionTest {

    private Gson gson = new Gson();


    @Test
    public void testConstructor() throws Exception {
        SourceWebsite website = new SourceWebsite();
        website.setVersion(2);

        String string = gson.toJson(website);
        SourceWebsite websiteLoad = gson.fromJson(string, SourceWebsite.class);

        assertThat(websiteLoad.getVersion()).isEqualTo(2);
    }
}