package munch.api;

import com.google.common.collect.ImmutableSet;
import com.typesafe.config.Config;
import munch.api.exception.UnsupportedException;
import org.apache.commons.lang3.StringUtils;
import spark.Request;

import javax.inject.Inject;
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

    private final ImmutableSet<String> supportedVersions;
    private final ImmutableSet<String> supportedBuilds;

    @Inject
    public SupportedVersions(Config config) {
        this.supportedVersions = ImmutableSet.copyOf(config.getString("api.supported.versions").split(","));
        this.supportedBuilds = ImmutableSet.copyOf(config.getString("api.supported.builds").split(","));
    }

    public void validate(Request request) {
        String version = request.headers(HEADER_VERSION);
        if (StringUtils.isNotBlank(version) && !supportedVersions.contains(version)) {
            throw new UnsupportedException();
        }

        String build = request.headers(HEADER_BUILD);
        if (StringUtils.isNotBlank(build) && !supportedBuilds.contains(build)) {
            throw new UnsupportedException();
        }

        // If build or version is not given, no error will be thrown
    }
}
