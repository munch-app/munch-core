package munch.images.persist;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.utils.file.ContentTypeError;
import munch.images.ResolveService;
import munch.images.database.Image;
import munch.images.database.ImageKind;
import munch.images.database.ImageMapper;
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
public class PersistService implements JsonService {

    private final ImageMapper mapper;
    private final MultipartConfigElement multipartConfig;

    @Inject
    public PersistService(ImageMapper mapper) {
        this.mapper = mapper;
        this.multipartConfig = new MultipartConfigElement("/temp");
    }

    @Override
    public void route() {
        PATH("/images", () -> {
            PUT("/:key/resize", this::resize);
            // Future: Delete types

            PUT("", this::put);
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
        Set<ImageKind> kinds = ResolveService.queryKinds(call);
        return mapper.resize(key, kinds);
    }

    /**
     * Create new image
     *
     * @param call json call
     * @return newly create image
     * @throws IOException      network error
     * @throws ContentTypeError content error
     */
    public Image put(JsonCall call) throws IOException, ContentTypeError, ServletException {
        Set<ImageKind> kinds = ResolveService.queryKinds(call);

        call.request().attribute("org.eclipse.jetty.multipartConfig", multipartConfig);
        Part part = call.request().raw().getPart("file");
        String fileName = part.getSubmittedFileName();

        try (InputStream inputStream = part.getInputStream()) {
            File file = File.createTempFile(RandomStringUtils.random(20), "");
            FileUtils.copyInputStreamToFile(inputStream, file);
            // Upload the image
            return mapper.upload(fileName, file, kinds);
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
