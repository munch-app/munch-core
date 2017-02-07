package com.munch.accounts.controller;

import com.munch.accounts.spark.SparkServer;
import spark.Spark;

/**
 * Created By: Fuxing Loh
 * Date: 7/2/2017
 * Time: 4:43 PM
 * Project: munch-core
 */
public class AssetsController extends SparkServer.Controller {

    @Override
    public void route() {
        Spark.staticFileLocation("/public");
    }

}
