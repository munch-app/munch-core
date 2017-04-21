package munch.images;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.utils.file.ContentTypeError;
import munch.images.database.Image;
import munch.images.database.ImageKind;
import munch.images.database.ImageMapper;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;
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
public class PersistService implements JsonService {

    private final ImageMapper mapper;
    private final MultipartConfigElement multipartConfig;
    private final Set<String> contentTypes;

    @Inject
    public PersistService(ImageMapper mapper) {
        this.mapper = mapper;
        this.multipartConfig = new MultipartConfigElement("/temp");
        this.contentTypes = ImmutableSet.of("image/jpeg", "image/png");
    }

    @Override
    public void route() {
        PATH("/images", () -> {
            PUT("/:key/resize", this::resize);
            // Future: Delete types

            Spark.put("", "multipart/form-data", this::put, toJson);
            DELETE("/:key", this::delete);
        });
    }

    /**
     * Add image kinds to image group
     *
     * @param call json call
     * @return null of image don't exist, or updated images
     */
    public Image resize(JsonCall call) throws ContentTypeError, IOException {
        String key = call.pathString("key");
        Set<ImageKind> kinds = ImageKind.resolveKinds(call.request().queryParams("kinds"));
        return mapper.resize(key, kinds);
    }

    /**
     * Create new image
     * multipart= file:
     *
     * @param request  Spark request
     * @param response Spark response
     * @return newly create image
     * @throws IOException      network error
     * @throws ContentTypeError content error
     */
    public Image put(Request request, Response response) throws IOException, ContentTypeError, ServletException {
        Set<ImageKind> kinds = ImageKind.resolveKinds(request.queryParams("kinds"));
        request.attribute("org.eclipse.jetty.multipartConfig", multipartConfig);
        Part part = request.raw().getPart("file");

        // Upload the image
        try (InputStream inputStream = part.getInputStream()) {
            String contentType = part.getContentType();
            if (!contentTypes.contains(contentType)) throw new ContentTypeNotImage(contentType);
            long length = part.getSize();
            return mapper.upload(inputStream, length, contentType, kinds);
        }
    }

    /**
     * @param call json call
     * @return 200 = success, else 500 for error
     * @throws ContentTypeError content parsing error
     * @throws IOException      network error
     */
    public JsonNode delete(JsonCall call) throws ContentTypeError, IOException {
        String key = call.pathString("key");
        mapper.delete(key);
        return Meta200;
    }
}
