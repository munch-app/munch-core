package munch.images;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.utils.file.ContentTypeError;
import munch.images.database.Image;
import munch.images.database.ImageKind;
import munch.images.database.ImageMapper;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 10:43 PM
 * Project: munch-core
 */
@Singleton
public class ImageService implements JsonService {

    private final ImageMapper mapper;

    @Inject
    public ImageService(ImageMapper mapper) {
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
    private List<Image> batchGet(JsonCall call) {
        List<String> keys = call.bodyAsList(String.class);
        Set<ImageKind> kinds = ImageKind.resolveKinds(call.queryString("kinds", null));
        return mapper.get(keys, kinds);
    }

    /**
     * Image id in path
     *
     * @param call json call
     * @return 200 = Image, 404 = not found
     */
    private Image get(JsonCall call) {
        String key = call.pathString("key");
        Set<ImageKind> kinds = ImageKind.resolveKinds(call.queryString("kinds", null));
        return mapper.get(key, kinds);
    }

    /**
     * @param call json call
     * @return 200 = success, else 500 for error
     * @throws ContentTypeError content parsing error
     * @throws IOException      network error
     */
    private JsonNode delete(JsonCall call) throws ContentTypeError, IOException {
        String key = call.pathString("key");
        mapper.delete(key);
        return Meta200;
    }
}
