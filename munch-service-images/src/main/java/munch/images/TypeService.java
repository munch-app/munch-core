package munch.images;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Singleton;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 11:01 PM
 * Project: munch-core
 */
@Singleton
public class TypeService implements JsonService {

    @Override
    public void route() {
        PATH("/images", () -> {
            PUT("/:key/type", this::add);
            // Future: Delete types
        });
    }

    /**
     * Add image type to image group
     *
     * @param call json call
     * @return TODO
     */
    public JsonNode add(JsonCall call) {
        String[] types = call.pathString("types").split(",");
        return null;
    }
}
