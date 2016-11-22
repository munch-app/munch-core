/*
 * Copyright (c) 2015. This file is part of 3LINES PTE. LTD. property that is used for project Puffin which is not to be released for personal use.
 * Failing to do so will result in heavy penalty. However, if this individual file might need to be taken out of context for other purposes within the company,
 * please do ask for permission.
 */

package com.munch.core.essential.util.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fuxing
 * Date: 3/1/2015
 * Time: 8:54 PM
 * Project: PuffinCore
 */
public final class MunchConfig implements ConfigReader {

    // Default Template Location
    static final String TEMPLATE_FILE_LOCATION = "META-INF/munch-config.xml";
    // Default Dev/Prod Env File Location
    static final String ENV_FILE_LOCATION = "MUNCH_APP_CONFIG";

    static MunchConfig configInstance;
    static Map<String, MunchConfig> configMap = new HashMap<>();

    private final List<ConfigReader> configList = new ArrayList<>();
    private final ConfigReader defaultReader;

    private MunchConfig(ConfigReader reader) {
        this.defaultReader = reader;
        this.configList.add(defaultReader);
    }

    public static void addConfigReader(File file) {
        addConfigReader(PropertiesReader.getReader(file));
    }

    public static void addConfigReader(ConfigReader reader) {
        getInstance().configList.add(0, reader);
    }

    public static void addConfigReader(Class clazz, String resourceName) {
        addConfigReader(PropertiesReader.getReaderFromResource(clazz, resourceName));
    }

    public static ConfigReader getDefaultInstance() {
        return getInstance().defaultReader;
    }

    /**
     * The default puffincore.xml file
     * With override configs
     *
     * @return XMLConfiguration
     */
    public static MunchConfig getInstance() {
        if (configInstance == null) {
            synchronized (MunchConfig.class) {
                if (configInstance == null) {
                    configInstance = new MunchConfig(getBuiltInReader(System.getenv(ENV_FILE_LOCATION)));
                }
            }
        }
        return configInstance;
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
                public long getLong(String key) {
                    return configuration.getLong(key, 0);
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
        return getString(key, null);
    }

    /**
     * Get boolean value with key from configuration file
     *
     * @param key String
     * @return value
     */
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * Get long value with key from configuration file
     *
     * @param key String
     * @return value
     */
    public long getLong(String key) {
        return getLong(key, 0);
    }

    /**
     * Get string value with key from configuration file
     *
     * @param key String
     * @return value
     */
    public String getString(String key, String defaultValue) {
        for (ConfigReader configReader : configList) {
            String value = configReader.getString(key);
            if (value != null) return value;
        }
        return defaultValue;
    }

    /**
     * Get boolean value with key from configuration file
     *
     * @param key String
     * @return value
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        for (ConfigReader configReader : configList) {
            String value = configReader.getString(key);
            if (value != null) return configReader.getBoolean(key);
        }
        return defaultValue;
    }

    /**
     * Get long value with key from configuration file
     *
     * @param key String
     * @return value
     */
    public long getLong(String key, long defaultValue) {
        for (ConfigReader configReader : configList) {
            String value = configReader.getString(key);
            if (value != null) return configReader.getLong(key);
        }
        return defaultValue;
    }
}
