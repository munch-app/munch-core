package com.munch.utils.spark;

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
}
