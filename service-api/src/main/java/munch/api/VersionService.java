package munch.api;

import com.google.common.collect.ImmutableSet;
import com.typesafe.config.Config;
import munch.api.exception.UnsupportedException;
import munch.restful.server.JsonService;
import org.apache.commons.lang3.StringUtils;
import spark.Request;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 15/9/2017
 * Time: 8:34 PM
 * Project: munch-core
 */
@Singleton
public final class VersionService implements JsonService {

    @Override
    public void route() {
        GET("/versions/validate", call -> nodes(200));
    }

    /**
     * Created By: Fuxing Loh
     * Date: 23/7/2017
     * Time: 4:03 PM
     * Project: munch-core
     */
    public static final class Validator {
        public static final String HEADER_VERSION = "Application-Version";
        public static final String HEADER_BUILD = "Application-Build";

        private final ImmutableSet<String> supportedVersions;

        @Inject
        public Validator(Config config) {
            String versions = config.getString("api.versions.supported");
            this.supportedVersions = ImmutableSet.copyOf(versions.split(" *, *"));
        }

        public void validate(Request request) {
            String version = request.headers(HEADER_VERSION);
            validate(version);
        }

        private void validate(String version) {
            if (StringUtils.isNotBlank(version) && !supportedVersions.contains(version)) {
                throw new UnsupportedException();
            }
            // If version is not given, no error will be thrown
        }
    }
}
