package com.munch.accounts.controller;

import org.pac4j.core.context.HttpConstants;
import org.pac4j.sparkjava.DefaultHttpActionAdapter;
import org.pac4j.sparkjava.SparkWebContext;
import spark.ModelAndView;
import spark.TemplateEngine;

import java.util.Collections;

import static spark.Spark.halt;

/**
 * Created by: Fuxing
 * Date: 5/2/2017
 * Time: 1:53 AM
 * Project: munch-core
 */
public class WebActionAdapter extends DefaultHttpActionAdapter {

    private final TemplateEngine templateEngine;

    /**
     * @param templateEngine template engine to render errors
     */
    WebActionAdapter(final TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public Object adapt(int code, SparkWebContext context) {
        if (code == HttpConstants.UNAUTHORIZED) {
            halt(401, templateEngine.render(new ModelAndView(Collections.emptyMap(), "error/401.hbs")));
        } else if (code == HttpConstants.FORBIDDEN) {
            halt(403, templateEngine.render(new ModelAndView(Collections.emptyMap(), "error/403.hbs")));
        } else {
            return super.adapt(code, context);
        }
        return null;
    }
}
