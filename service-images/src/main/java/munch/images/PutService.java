package munch.images;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.utils.file.ContentTypeError;
import com.typesafe.config.Config;
import munch.images.database.ImageMapper;
import munch.images.database.ImageMeta;
import munch.images.database.ImageType;
import munch.restful.server.JsonService;
import munch.restful.server.exceptions.StructuredException;
import spark.Request;
import spark.Response;
import spark.Spark;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
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
                Spark.put("", "multipart/form-data", this::put, toJson);
            } else {
                Spark.put("", "multipart/form-data", (req, res) -> {
                    throwError("ThumborNotAvailable", "Required thumbor instance not available.", 501);
                    return null;
                }, toJson);
            }
        });
    }

    /**
     * Create new image
     * multipart = file
     *
     * @param request  Spark request
     * @param response Spark response
     * @return newly create image
     * @throws IOException      network error
     * @throws ContentTypeError content error
     */
    private ImageMeta put(Request request, Response response) throws IOException, ContentTypeError, ServletException {
        Set<ImageType> kinds = ImageType.resolveKinds(request.queryParams("kinds"));
        if (kinds == null) kinds = ImageType.DEFAULT_KINDS;

        // Get file part
        request.attribute("org.eclipse.jetty.multipartConfig", multipartConfig);
        Part part = request.raw().getPart("file");

        // Upload the image
        try (InputStream inputStream = part.getInputStream()) {
            String contentType = part.getContentType();
            if (!contentTypes.contains(contentType)) throw new NotImageException(contentType);
            long length = part.getSize();
            return mapper.upload(inputStream, length, contentType, kinds);
        }
    }

    /**
     * Content type not image exception
     */
    private static class NotImageException extends StructuredException {
        private NotImageException(String contentType) {
            super("NotImageException", "Uploaded content type must be image. (" + contentType + ")", 400);
        }
    }
}
