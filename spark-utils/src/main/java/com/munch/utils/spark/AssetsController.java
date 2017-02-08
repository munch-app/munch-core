package com.munch.utils.spark;

import spark.Spark;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created By: Fuxing Loh
 * Date: 8/2/2017
 * Time: 3:28 PM
 * Project: munch-core
 */
public class AssetsController implements SparkRouter {

    private final String internal;
    private final String external;

    /**
     * external directory ignored
     *
     * @param internal internal resource directory
     */
    public AssetsController(String internal) {
        this(internal, null);
    }

    /**
     * @param internal internal resource directory
     * @param external external directory, null = ignore
     */
    public AssetsController(String internal, String external) {
        this.internal = internal;
        this.external = external;
    }

    /**
     * @throws RuntimeException external location cannot be initialized
     */
    @Override
    public void route() {
        Spark.staticFileLocation(internal);

        try {
            if (external != null) {
                Files.createDirectories(Paths.get(external));
                Spark.externalStaticFileLocation(external);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
