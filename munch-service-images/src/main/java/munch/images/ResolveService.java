package munch.images;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.images.database.Image;
import munch.images.database.ImageKind;
import munch.images.database.ImageMapper;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
        Set<ImageKind> kinds = queryKinds(call);
        return mapper.get(key, kinds);
    }

    public List<Image> batch(JsonCall call) {
        List<String> keys = call.bodyAsList(String.class);
        Set<ImageKind> kinds = queryKinds(call);

        return mapper.get(keys, kinds);
    }

    /**
     * @param call json call
     * @return types in String array
     */
    public static Set<ImageKind> queryKinds(JsonCall call) {
        return Optional.ofNullable(call.request().queryParams("kinds"))
                .map(s -> Arrays.stream(s.split(",")).map(ImageKind::forValue).collect(Collectors.toSet()))
                .orElse(ImmutableSet.of());
    }
}
