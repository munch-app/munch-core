package munch.images;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.images.database.Image;
import munch.images.database.ImageMapper;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
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
        Set<String> types = mapTypes(call);

        Image image = mapper.get(key);
        if (types != null)
            image.getTypes().removeIf(type -> !types.contains(type.getType().getName()));
        return image;
    }

    public List<Image> batch(JsonCall call) {
        List<String> keys = call.bodyAsList(String.class);
        Set<String> types = mapTypes(call);

        List<Image> images = mapper.get(keys);
        if (types != null) {
            for (Image image : images) {
                image.getTypes().removeIf(type -> !types.contains(type.getType().getName()));
            }
        }
        return images;
    }

    /**
     * @param call json call
     * @return types in String array
     */
    @Nullable
    public static Set<String> mapTypes(JsonCall call) {
        return Optional.of(call.request().queryParams("types"))
                .map(s -> s.split(","))
                .map(ImmutableSet::copyOf)
                .orElse(null);
    }
}
