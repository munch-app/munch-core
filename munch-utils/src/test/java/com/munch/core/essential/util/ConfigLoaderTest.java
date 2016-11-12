/*
 * Copyright (c) 2015. This file is part of 3LINES PTE. LTD. property that is used for project Puffin which is not to be released for personal use.
 * Failing to do so will result in heavy penalty. However, if this individual file might need to be taken out of context for other purposes within the company,
 * please do ask for permission.
 */

package com.munch.core.essential.util;

import com.munch.core.essential.util.config.MunchConfig;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by Fuxing
 * Date: 22/1/2015
 * Time: 11:23 PM
 * Project: PuffinCore
 */
public class ConfigLoaderTest {

    @Test
    public void normalUse() {
        MunchConfig config = MunchConfig.getInstance();
        testGets(config);
        config.isDev();
        config.isProd();
        assertThat(config.getString("environment")).isEqualTo("development");
    }

    /**
     * Tests values get method
     *
     * @param config config to test with
     */
    private void testGets(MunchConfig config) {
        assertThat(config.getBoolean("key")).isFalse();
        assertThat(config.getString("key")).isNull();
        assertThat(config.getInt("key")).isEqualTo(0);

        assertThat(config.isDev()).isNotEqualTo(config.isProd());
    }
}
