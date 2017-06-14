package munch.clients;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import munch.restful.client.RestfulClient;
import munch.restful.client.RestfulResponse;
import org.apache.http.entity.ContentType;

import javax.inject.Named;
import java.io.File;
import java.io.InputStream;

/**
 * Created by: Fuxing
 * Date: 2/6/2017
 * Time: 7:43 AM
 * Project: munch-core
 */
@Singleton
public class ImageClient extends RestfulClient {

    @Inject
    public ImageClient(@Named("services") Config config) {
        super(config.getString("images.url"));
    }

    /**
     * @param key image key
     * @return ImageMeta
     */
    public ImageMeta get(String key) {
        return doGet("/images/{key}")
                .path("key", key)
                .hasMetaCodes(200, 404)
                .asDataObject(ImageMeta.class);
    }

    public ImageMeta put(File file, ContentType contentType) {
        return doPut("/images")
                .field("file", file, contentType.getMimeType())
                .hasMetaCodes(200)
                .asDataObject(ImageMeta.class);
    }

    public ImageMeta put(InputStream stream, ContentType contentType, String fileName) throws NotImageException {
        return doPut("/images")
                .field("file", stream, contentType, fileName)
                .hasMetaCodes(200)
                .handle(NotImageException::handle)
                .asDataObject(ImageMeta.class);
    }

    public ImageMeta put(InputStream stream, String fileName) throws NotImageException {
        return put(stream, ContentType.APPLICATION_OCTET_STREAM, fileName);
    }

    /**
     * @param key key of image to delete
     */
    public void delete(String key) {
        doDelete("/images/{key}")
                .path("key", key)
                .hasMetaCodes(200);
    }

    public static class NotImageException extends RuntimeException {
        private final String type;

        public NotImageException(String type, String message) {
            super(message);
            this.type = type;
        }

        public String getType() {
            return type;
        }

        /**
         * Default data client error handling
         *
         * @param response response to handle
         */
        public static void handle(RestfulResponse response) {
            if (response.hasErrorType("NotImageException")) {
                JsonNode meta = response.getMetaNode();
                throw new NotImageException(response.getErrorType(), meta.path("errorMessage").asText());
            }
        }
    }
}

