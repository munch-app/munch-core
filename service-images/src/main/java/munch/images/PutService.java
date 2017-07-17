package munch.images;

import catalyst.utils.exception.PredicateRetriable;
import catalyst.utils.exception.Retriable;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.utils.file.ContentTypeError;
import com.munch.utils.file.FileTypeUtils;
import com.typesafe.config.Config;
import munch.images.database.ImageMapper;
import munch.images.database.ImageMeta;
import munch.images.database.ImageType;
import munch.images.exceptions.ImagePutException;
import munch.images.exceptions.NotImageException;
import munch.images.exceptions.ThumborNotAvailable;
import munch.restful.core.exception.ValidationException;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Spark;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 11:01 PM
 * Project: munch-core
 */
@Singleton
public class PutService implements JsonService {
    private static final Logger logger = LoggerFactory.getLogger(PutService.class);
    private static final Retriable retriable = new PredicateRetriable(6, Duration.ofSeconds(2),
            t -> t.getMessage().contains("Server returned HTTP response code: 403")) {
        @Override
        public void log(Throwable exception, int executionCount) {
        }
    };

    private final Config config;
    private final ImageMapper mapper;
    private final MultipartConfigElement multipartConfig;
    private final Set<String> contentTypes;

    @Inject
    public PutService(Config config, ImageMapper mapper) {
        this.config = config;
        this.mapper = mapper;
        this.multipartConfig = new MultipartConfigElement("/temp");
        this.contentTypes = ImmutableSet.of("image/jpeg", "image/png");
    }

    @Override
    public void route() {
        PATH("/images", () -> {
            Spark.before("", this::validate);
            Spark.before("/url", this::validate);

            PUT("", "multipart/form-data", this::put);
            PUT("/url", this::putUrl);
        });
    }

    /**
     * Validate that thumbor exist
     *
     * @param request  spark request
     * @param response spark response
     */
    private void validate(Request request, Response response) {
        // Check if thumbor exists
        if (!config.hasPath("image.thumbor.url")) {
            throw new ThumborNotAvailable();
        }
    }

    /**
     * Create new image
     * multipart = file
     *
     * @param call Json call
     * @return newly create image
     * @throws IOException      network error
     * @throws ContentTypeError content error
     */
    private ImageMeta put(JsonCall call) throws IOException, ContentTypeError, ServletException {
        Set<ImageType> kinds = ImageType.resolveKinds(
                call.queryString("kinds", null), ImageType.DEFAULT_KINDS);

        // Get file part
        call.request().attribute("org.eclipse.jetty.multipartConfig", multipartConfig);
        Part part = call.request().raw().getPart("file");

        // Upload the image
        File temp = File.createTempFile(RandomStringUtils.randomAlphabetic(20), part.getName());
        try (InputStream inputStream = part.getInputStream()) {
            // Convert to file
            FileUtils.copyInputStreamToFile(inputStream, temp);

            // Check contentType, if is octet then convert to actual
            String contentType = part.getContentType();
            if (contentType.equals("application/octet-stream"))
                contentType = FileTypeUtils.getContentType(part.getName(), temp);

            // Validate content type
            if (!contentTypes.contains(contentType)) throw new NotImageException(contentType);
            return mapper.upload(temp, contentType, kinds);
        } finally {
            FileUtils.deleteQuietly(temp);
        }
    }

    /**
     * <pre>
     * {
     *     "url": "url of image"
     * }
     * </pre>
     *
     * @param call Json call
     * @return newly create image
     */
    private ImageMeta putUrl(JsonCall call, JsonNode request) {
        Set<ImageType> kinds = ImageType.resolveKinds(
                call.queryString("kinds", null), ImageType.DEFAULT_KINDS);

        String urlString = request.path("url").asText(null);
        ValidationException.requireNonBlank("url", urlString);

        try {
            URL url = new URL(urlString);
            // Open connect download and return
            return retriable.loop(() -> {
                URLConnection connection = url.openConnection();
                connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");

                // Validate is allowed content type
                // 1: Here can throw IOE
                String contentType = connection.getContentType();
                if (!contentTypes.contains(contentType)) throw new NotImageException(contentType);

                File temp = File.createTempFile(RandomStringUtils.randomAlphabetic(20), ".tmp");
                try (InputStream inputStream = connection.getInputStream()) {
                    FileUtils.copyInputStreamToFile(inputStream, temp);
                    // 2: Here can throw IOE
                    return mapper.upload(temp, contentType, kinds);
                } finally {
                    FileUtils.deleteQuietly(temp);
                }
            });
        } catch (Exception ioe) {
            throw new ImagePutException("Failed to put image for url: " + urlString + ", error: " + ioe.getMessage());
        }
    }


}
