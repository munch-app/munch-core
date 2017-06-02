package munch.clients;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import munch.restful.client.RestfulClient;
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

    public ImageMeta put(File file) {
        return doPut("/images")
                .field("file", file)
                .hasMetaCodes(200)
                .asDataObject(ImageMeta.class);
    }

    public ImageMeta put(InputStream stream, ContentType contentType, String fileName) {
        return doPut("/images")
                .field("file", stream, contentType, fileName)
                .hasMetaCodes(200)
                .asDataObject(ImageMeta.class);
    }

    /**
     * @param key key of image to delete
     */
    public void delete(String key) {
        doDelete("/images/{key}")
                .path("key", key)
                .hasMetaCodes(200);
    }
}

