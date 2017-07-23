package munch.api;

import com.google.common.collect.ImmutableSet;
import munch.api.exception.UnsupportedException;
import spark.Request;

import javax.inject.Singleton;

/**
 * Created By: Fuxing Loh
 * Date: 23/7/2017
 * Time: 4:03 PM
 * Project: munch-core
 */
@Singleton
public final class SupportedVersions {
    public static final String HEADER_VERSION = "Application-Version";
    public static final String HEADER_BUILD = "Application-Build";

    private static final ImmutableSet<String> supportedVersions = ImmutableSet.of("0.1");
    private static final ImmutableSet<String> supportedBuilds = ImmutableSet.of("6");

    public void validate(Request request) {
        String version = request.headers(HEADER_VERSION);
        if (!supportedVersions.contains(version)) {
            throw new UnsupportedException();
        }

        String build = request.headers(HEADER_BUILD);
        if (!supportedBuilds.contains(build)) {
            throw new UnsupportedException();
        }
    }
}
