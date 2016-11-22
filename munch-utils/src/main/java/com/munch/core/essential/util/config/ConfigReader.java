/*
 * Copyright (c) 2015. This file is part of 3LINES PTE. LTD. property that is used for project Puffin which is not to be released for personal use.
 * Failing to do so will result in heavy penalty. However, if this individual file might need to be taken out of context for other purposes within the company,
 * please do ask for permission.
 */

package com.munch.core.essential.util.config;

/**
 * Default Reader implementation
 */
public interface ConfigReader extends PropertiesReader {

    /**
     * Environment reader
     *
     * @return bool
     */
    default boolean isDev() {
        return getString("environment").equalsIgnoreCase("development");
    }

    /**
     * Environment reader
     *
     * @return bool
     */
    default boolean isProd() {
        return getString("environment").equalsIgnoreCase("production");
    }
}
