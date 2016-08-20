/*
 * Copyright (c) 2015. This file is part of 3LINES PTE. LTD. property that is used for project Puffin which is not to be released for personal use.
 * Failing to do so will result in heavy penalty. However, if this individual file might need to be taken out of context for other purposes within the company,
 * please do ask for permission.
 */

package com.munch.core.essential.util;

/**
 * Default Reader implementation
 */
public interface ConfigReader {

    /**
     * Get value from key
     *
     * @param key getInstance
     * @return value
     */
    String getString(String key);

    /**
     * Get boolean from key
     *
     * @param key getInstance
     * @return boolean value
     */
    boolean getBoolean(String key);

    /**
     * Get int from key
     *
     * @param key getInstance
     * @return int value
     */
    int getInt(String key);

    /**
     * Environment reader
     *
     * @return bool
     */
    boolean isDev();

    /**
     * Environment reader
     *
     * @return bool
     */
    boolean isProd();
}
