package com.munch.accounts.controller;

import org.pac4j.core.context.HttpConstants;
import org.pac4j.sparkjava.DefaultHttpActionAdapter;
import org.pac4j.sparkjava.SparkWebContext;
import spark.ModelAndView;
import spark.TemplateEngine;

import java.util.HashMap;

import static spark.Spark.halt;

/**
 * Created by: Fuxing
 * Date: 5/2/2017
 * Time: 1:53 AM
 * Project: munch-core
 */
public class WebActionAdapter extends DefaultHttpActionAdapter {

    private final TemplateEngine templateEngine;

    public WebActionAdapter(final TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public Object adapt(int code, SparkWebContext context) {
        // TODO better halter
        if (code == HttpConstants.UNAUTHORIZED) {
            halt(401, templateEngine.render(new ModelAndView(new HashMap<>(), "error401.mustache")));
        } else if (code == HttpConstants.FORBIDDEN) {
            halt(403, templateEngine.render(new ModelAndView(new HashMap<>(), "error403.mustache")));
        } else {
            return super.adapt(code, context);
        }
        return null;
    }
}
