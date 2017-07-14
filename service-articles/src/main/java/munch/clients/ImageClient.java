package munch.clients;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import munch.restful.client.RestfulClient;
import munch.restful.client.RestfulResponse;
import munch.restful.core.exception.StructuredException;
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
                .asDataObject(ImageMeta.class);
    }

    public ImageMeta put(String imageUrl) throws ImageException {
        return doPut("/images/url")
                .body(objectMapper.createObjectNode()
                        .put("url", imageUrl))
                .asResponse(ImageException::handle)
                .asDataObject(ImageMeta.class);
    }

    public ImageMeta put(File file, ContentType contentType) throws ImageException {
        return doPut("/images")
                .field("file", file, contentType.getMimeType())
                .asResponse(ImageException::handle)
                .asDataObject(ImageMeta.class);
    }

    public ImageMeta put(InputStream stream, ContentType contentType, String fileName) throws ImageException {
        return doPut("/images")
                .field("file", stream, contentType, fileName)
                .asResponse(ImageException::handle)
                .asDataObject(ImageMeta.class);
    }

    public ImageMeta put(InputStream stream, String fileName) throws ImageException {
        return put(stream, ContentType.APPLICATION_OCTET_STREAM, fileName);
    }

    /**
     * @param key key of image to delete
     */
    public void delete(String key) {
        doDelete("/images/{key}")
                .path("key", key)
                .asResponse();
    }

    public static class ImageException extends StructuredException {
        ImageException(StructuredException e) {
            super(e);
        }

        static void handle(RestfulResponse r, StructuredException e) {
            if (e != null && e.hasType("NotImageException", "ImagePutException")) throw new ImageException(e);
        }
    }
}

