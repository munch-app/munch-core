package munch.images.persist;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.utils.file.ContentTypeError;
import munch.images.ResolveService;
import munch.images.database.Image;
import munch.images.database.ImageMapper;
import munch.images.database.TypeDescription;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;
import org.apache.tika.mime.MimeTypeException;

import java.io.IOException;
import java.util.Arrays;
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

    @Inject
    public PersistService(ImageMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void route() {
        PATH("/images", () -> {
            PUT("/:key/type", this::add);
            // Future: Delete types

            PUT("", this::put);
            DELETE("/:key", this::delete);
        });
    }

    /**
     * Add image type to image group
     *
     * @param call json call
     * @return updated Image
     */
    public Image add(JsonCall call) throws ContentTypeError, IOException, MimeTypeException {
        String key = call.pathString("key");
        String[] types = call.pathString("types").split(",");

        // Generate description if not already exist
        Image image = mapper.get(key);
        Arrays.stream(types).map(TypeDescription::forValue).forEach(description -> {
            if (image.getTypes().stream().noneMatch(type -> type.getType() == description)) {
                Image.Type type = new Image.Type();
                type.setType(description);
                image.getTypes().add(type);
            }
        });

        mapper.put(image);
        return image;
    }

    public Image put(JsonCall call) throws IOException, ContentTypeError, MimeTypeException {
        Set<String> types = ResolveService.mapTypes(call);

        // Create image and save it
        if (types == null) {
            // TODO
            return mapper.create("", "", null);
        } else {
            TypeDescription[] descriptions = types.stream().map(TypeDescription::forValue).toArray(TypeDescription[]::new);
            return mapper.create("", "", null, descriptions);
        }
    }

    public JsonNode delete(JsonCall call) throws ContentTypeError, IOException, MimeTypeException {
        String key = call.pathString("key");
        mapper.delete(key);
        return Meta200;
    }
}
