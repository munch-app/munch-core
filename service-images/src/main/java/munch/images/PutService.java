package munch.images;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.utils.file.ContentTypeError;
import com.munch.utils.file.FileTypeUtils;
import com.typesafe.config.Config;
import munch.images.database.ImageMapper;
import munch.images.database.ImageMeta;
import munch.images.database.ImageType;
import munch.restful.core.exception.StructuredException;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 11:01 PM
 * Project: munch-core
 */
@Singleton
public class PutService implements JsonService {

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
            // Check if thumbor exists
            if (config.hasPath("image.thumbor.url")) {
                PUT("", "multipart/form-data", this::put);
            } else {
                PUT("", "multipart/form-data", call -> {
                    throw new ThumborNotAvailable();
                });
            }
        });
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
        Set<ImageType> kinds = ImageType.resolveKinds(call.queryString("kinds", null));
        if (kinds.isEmpty()) kinds = ImageType.DEFAULT_KINDS;

        // Get file part
        call.request().attribute("org.eclipse.jetty.multipartConfig", multipartConfig);
        Part part = call.request().raw().getPart("file");

        // Upload the image
        try (InputStream inputStream = part.getInputStream()) {
            // Convert to file
            File temp = File.createTempFile(RandomStringUtils.randomAlphabetic(20), part.getName());
            FileUtils.copyInputStreamToFile(inputStream, temp);

            // Check contentType, if is octet then convert to actual
            String contentType = part.getContentType();
            if (contentType.equals("application/octet-stream"))
                contentType = FileTypeUtils.getContentType(part.getName(), temp);

            // Validate content type
            if (!contentTypes.contains(contentType)) throw new NotImageException(contentType);
            return mapper.upload(temp, contentType, kinds);
        }
    }

    /**
     * Content type not image exception
     */
    private static class NotImageException extends StructuredException {
        private NotImageException(String contentType) {
            super(400, "NotImageException", "Uploaded content type must be image. (" + contentType + ")");
        }
    }
}
