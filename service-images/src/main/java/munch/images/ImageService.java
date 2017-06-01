package munch.images;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.utils.file.ContentTypeError;
import munch.images.database.ImageMeta;
import munch.images.database.ImageMapper;
import munch.images.database.MetaDatabase;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;

import java.io.IOException;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 10:43 PM
 * Project: munch-core
 */
@Singleton
public class ImageService implements JsonService {

    private final MetaDatabase database;
    private final ImageMapper mapper;

    @Inject
    public ImageService(MetaDatabase database, ImageMapper mapper) {
        this.database = database;
        this.mapper = mapper;
    }

    @Override
    public void route() {
        PATH("/images", () -> {
            POST("/get", this::batchGet);

            PATH("/:key", () -> {
                GET("", this::get);
                DELETE("", this::delete);
            });
        });
    }

    /**
     * Input = Collection of image keys
     *
     * @param call json call
     * @return Collection of Images
     */
    private List<ImageMeta> batchGet(JsonCall call) {
        return database.get(call.bodyAsList(String.class));
    }

    /**
     * Image id in path
     *
     * @param call json call
     * @return 200 = Image, 404 = not found
     */
    private ImageMeta get(JsonCall call) {
        return database.get(call.pathString("key"));
    }

    /**
     * @param call json call
     * @return 200 = success, else 500 for error
     * @throws ContentTypeError content parsing error
     * @throws IOException      network error
     */
    private JsonNode delete(JsonCall call) throws ContentTypeError, IOException {
        mapper.delete(call.pathString("key"));
        return Meta200;
    }
}
