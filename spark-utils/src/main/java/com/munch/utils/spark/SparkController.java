package com.munch.utils.spark;

import spark.ModelAndView;
import spark.TemplateEngine;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.Collections;
import java.util.Map;

/**
 * Created By: Fuxing Loh
 * Date: 8/2/2017
 * Time: 2:24 PM
 * Project: munch-core
 */
public interface SparkController extends SparkRouter {
    TemplateEngine templateEngine = new HandlebarsTemplateEngine();
    Map EmptyMap = Collections.emptyMap();

    /**
     * @param key    key of object
     * @param object object
     * @param <T>    Type
     * @return Single map
     */
    default <T> Map<String, T> singleMap(String key, T object) {
        return Collections.singletonMap(key, object);
    }

    /**
     * @param key    key of object
     * @param object object
     * @param view   view name
     * @return ModelAndView with singleton map
     */
    default ModelAndView singleView(String key, Object object, String view) {
        return new ModelAndView(singleMap(key, object), view);
    }

    /**
     * @param name view name
     * @return ModelAndView with empty name
     */
    default ModelAndView view(String name) {
        return new ModelAndView(EmptyMap, name);
    }
}
