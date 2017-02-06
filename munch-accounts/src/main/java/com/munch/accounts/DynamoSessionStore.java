package com.munch.accounts;

import org.pac4j.core.context.session.SessionStore;
import org.pac4j.sparkjava.SparkWebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by: Fuxing
 * Date: 6/2/2017
 * Time: 6:49 PM
 * Project: munch-core
 */
public class DynamoSessionStore implements SessionStore<SparkWebContext> {
    private static final Logger logger = LoggerFactory.getLogger(DynamoSessionStore.class);

    @Override
    public String getOrCreateSessionId(SparkWebContext context) {
        return null;
    }

    @Override
    public Object get(SparkWebContext context, String key) {
        return null;
    }

    @Override
    public void set(SparkWebContext context, String key, Object value) {

    }

    @Override
    public void invalidateSession(SparkWebContext context) {

    }
}
