/*
 * Copyright (c) 2015. This file is part of 3LINES PTE. LTD. property that is used for project Puffin which is not to be released for personal use.
 * Failing to do so will result in heavy penalty. However, if this individual file might need to be taken out of context for other purposes within the company,
 * please do ask for permission.
 */

package com.munch.core.essential.util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fuxing
 * Date: 3/1/2015
 * Time: 8:54 PM
 * Project: PuffinCore
 */
public final class MunchConfig implements ConfigReader {

    // Default Template Location
    static final String TEMPLATE_FILE_LOCATION = "META-INF/munch-getInstance.xml";
    // Default Dev/Prod Env File Location
    static final String ENV_FILE_LOCATION = "MUNCH_APP_CONFIG";


    static MunchConfig defaultConfig;
    static Map<String, MunchConfig> configMap = new HashMap<>();
    private ConfigReader defaultReader;
    private ConfigReader fallbackReader;

    public MunchConfig(ConfigReader reader) {
        this.defaultReader = reader;
    }

    public MunchConfig(ConfigReader reader, ConfigReader fallbackReader) {
        this.defaultReader = reader;
        this.fallbackReader = fallbackReader;
    }

    /**
     * Override existing getInstance with implementable reader
     *
     * @param reader          implementable reader
     * @param defaultFallback to fall back to old reader
     */
    public static void override(ConfigReader reader, boolean defaultFallback) {
        synchronized (MunchConfig.class) {
            if (defaultFallback) {
                defaultConfig = new MunchConfig(reader, getBuiltInReader(System.getenv(ENV_FILE_LOCATION)));
            } else {
                defaultConfig = new MunchConfig(reader);
            }

        }
    }

    /**
     * The default puffincore.xml file
     * With override configs
     * @return XMLConfiguration
     */
    public static MunchConfig getInstance() {
        if (defaultConfig == null) {
            synchronized (MunchConfig.class) {
                if (defaultConfig == null) {
                    defaultConfig = new MunchConfig(getBuiltInReader(System.getenv(ENV_FILE_LOCATION)));
                }
            }
        }
        return defaultConfig;
    }

    /**
     * Config with named system
     * Get getInstance with their out container, doesn't interfere with the main getInstance system
     *
     * @return XMLConfiguration
     */
    public static MunchConfig getInstance(String fileLocation) {
        if (!configMap.containsKey(fileLocation)) {
            synchronized (MunchConfig.class) {
                if (!configMap.containsKey(fileLocation)) {
                    configMap.put(fileLocation, new MunchConfig(getBuiltInReader(fileLocation)));
                }
            }
        }
        return configMap.get(fileLocation);
    }

    static ConfigReader getBuiltInReader(final String fileLocation) {
        try {
            return new ConfigReader() {
                XMLConfiguration configuration = new XMLConfiguration(fileLocation);

                @Override
                public String getString(String key) {
                    return configuration.getString(key, null);
                }

                @Override
                public boolean getBoolean(String key) {
                    return configuration.getBoolean(key, false);
                }

                @Override
                public int getInt(String key) {
                    return configuration.getInt(key, 0);
                }

                @Override
                public boolean isDev() {
                    return getString("environment").equalsIgnoreCase("development");
                }

                @Override
                public boolean isProd() {
                    return getString("environment").equalsIgnoreCase("production");
                }
            };
        } catch (ConfigurationException ex) {
            // Error, if can't setup can't proceed
            throw new RuntimeException(ex);
        }
    }

    /**
     * Get string value with key from configuration file
     *
     * @param key String
     * @return value
     */
    public String getString(String key) {
        String value = defaultReader.getString(key);
        if (value == null && fallbackReader != null) {
            value = fallbackReader.getString(key);
        }
        return value;
    }

    /**
     * Get boolean value with key from configuration file
     *
     * @param key String
     * @return value
     */
    public boolean getBoolean(String key) {
        boolean value = defaultReader.getBoolean(key);
        if (defaultReader.getString(key) == null && fallbackReader != null) {
            value = fallbackReader.getBoolean(key);
        }
        return value;
    }

    /**
     * Get integer value with key from configuration file
     *
     * @param key String
     * @return value
     */
    public int getInt(String key) {
        int value = defaultReader.getInt(key);
        if (defaultReader.getString(key) == null && fallbackReader != null) {
            value = fallbackReader.getInt(key);
        }
        return value;
    }

    /**
     * Check the environment
     *
     * @return bool
     */
    public boolean isDev() {
        return defaultReader.isDev();
    }

    /**
     * Check the environment
     *
     * @return bool
     */
    public boolean isProd() {
        return defaultReader.isProd();
    }
}
