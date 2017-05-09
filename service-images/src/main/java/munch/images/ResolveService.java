package munch.images;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.images.database.Image;
import munch.images.database.ImageKind;
import munch.images.database.ImageMapper;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;

import java.util.List;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 10:43 PM
 * Project: munch-core
 */
@Singleton
public class ResolveService implements JsonService {

    private final ImageMapper mapper;

    @Inject
    public ResolveService(ImageMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void route() {
        PATH("/images", () -> {
            GET("/:key", this::get);

            PATH("/batch", () -> {
                POST("/get", this::batch);
            });
        });
    }

    public Image get(JsonCall call) {
        String key = call.pathString("key");
        Set<ImageKind> kinds = ImageKind.resolveKinds(call.request().queryParams("kinds"));
        return mapper.get(key, kinds);
    }

    public List<Image> batch(JsonCall call) {
        List<String> keys = call.bodyAsList(String.class);
        Set<ImageKind> kinds = ImageKind.resolveKinds(call.request().queryParams("kinds"));
        return mapper.get(keys, kinds);
    }
}
