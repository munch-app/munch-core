package com.munch.core.essential.util.config;

import org.apache.commons.configuration.XMLConfiguration;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by: Fuxing
 * Date: 23/10/2016
 * Time: 9:11 AM
 * Project: essential
 */
public interface PropertiesReader {

    /**
     * Get string from key
     *
     * @param key key value
     * @return string default: null
     */
    String getString(String key);

    /**
     * Get boolean from key
     *
     * @param key key value
     * @return boolean default: false
     */
    boolean getBoolean(String key);

    /**
     * Get int from key
     *
     * @param key key value
     * @return int default: 0
     */
    int getInt(String key);

    @SuppressWarnings("ConstantConditions")
    static PropertiesReader getReaderFromResource(Class clazz, String resourceName) {
        try {
            return getReader(new File(clazz.getClassLoader().getResource(resourceName).getFile()));
        } catch (NullPointerException e) {
            throw new RuntimeException(new FileNotFoundException("Resource file not found: " + e.getMessage()));
        }
    }

    static PropertiesReader getReader(File file) {
        try {
            return new PropertiesReader() {
                XMLConfiguration configuration = new XMLConfiguration(file);

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

            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
