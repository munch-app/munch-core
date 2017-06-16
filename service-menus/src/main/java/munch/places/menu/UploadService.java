package munch.places.menu;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.utils.file.ContentTypeError;
import munch.places.menu.data.MenuMapper;
import munch.restful.core.exception.StructuredException;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 21/4/2017
 * Time: 9:09 PM
 * Project: munch-core
 */
@Singleton
public class UploadService implements JsonService {

    private final MenuMapper mapper;

    private final MultipartConfigElement multipartConfig;
    private final Set<String> imageTypes;
    private final Set<String> pdfTypes;

    @Inject
    public UploadService(MenuMapper mapper) {
        this.mapper = mapper;
        this.multipartConfig = new MultipartConfigElement("/temp");
        this.imageTypes = ImmutableSet.of("image/jpeg", "image/png");
        this.pdfTypes = ImmutableSet.of("application/pdf");
    }

    @Override
    public void route() {
        PATH("/places/:placeId/menu", () -> {
            PUT("/:menuId/image", "multipart/form-data", this::putImage);
            PUT("/:menuId/pdf", "multipart/form-data", this::putPdf);
            PUT("/:menuId/website", this::putWebsite);

            DELETE("/:menuId", this::delete);
        });
    }

    private JsonNode putImage(JsonCall call) throws IOException, ServletException, ContentTypeError {
        String placeId = call.pathString("placeId");
        String menuId = call.pathString("menuId");
        call.request().attribute("org.eclipse.jetty.multipartConfig", multipartConfig);
        Part part = call.request().raw().getPart("file");

        // Upload image
        try (InputStream inputStream = part.getInputStream()) {
            String contentType = part.getContentType();
            if (!imageTypes.contains(contentType)) throw new NotMatchException(contentType);
            long length = part.getSize();
            mapper.putImage(placeId, menuId, inputStream, length, contentType);
        }
        return Meta200;
    }

    private JsonNode putPdf(JsonCall call) throws IOException, ServletException, ContentTypeError {
        String placeId = call.pathString("placeId");
        String menuId = call.pathString("menuId");
        call.request().attribute("org.eclipse.jetty.multipartConfig", multipartConfig);
        Part part = call.request().raw().getPart("file");

        // Upload pdf
        try (InputStream inputStream = part.getInputStream()) {
            String contentType = part.getContentType();
            if (!pdfTypes.contains(contentType)) throw new NotMatchException(contentType);
            long length = part.getSize();
            mapper.putPdf(placeId, menuId, inputStream, length, contentType);
        }
        return Meta200;
    }

    private JsonNode putWebsite(JsonCall call, JsonNode request) {
        String placeId = call.pathString("placeId");
        String menuId = call.pathString("menuId");
        String website = request.get("website").asText();
        mapper.putWebsite(placeId, menuId, website);
        return Meta200;
    }

    private JsonNode delete(JsonCall call) {
        String menuId = call.pathString("menuId");
        mapper.delete(menuId);
        return Meta200;
    }

    public static class NotMatchException extends StructuredException {
        protected NotMatchException(String contentType) {
            super(400, "ContentTypeNotMatchException", "Uploaded content type must be image. (" + contentType + ")");
        }
    }
}
