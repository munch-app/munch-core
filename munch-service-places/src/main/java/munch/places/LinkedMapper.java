package munch.places;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.places.data.ImageLink;
import munch.places.data.ImageLinkDatabase;
import munch.places.data.Place;
import munch.restful.server.JsonCall;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 23/4/2017
 * Time: 1:29 PM
 * Project: munch-core
 */
@Singleton
public class LinkedMapper {

    private final ImageLinkDatabase imageDatabase;
    private final ObjectMapper objectMapper;

    @Inject
    public LinkedMapper(ImageLinkDatabase imageDatabase, ObjectMapper objectMapper) {
        this.imageDatabase = imageDatabase;
        this.objectMapper = objectMapper;
    }

    /**
     * ImageLink:
     * linked.image=true
     * linked.image.size=10
     *
     * @param call   json call to read query string
     * @param places list of places to map
     * @return mapped to array node
     */
    public ArrayNode fill(JsonCall call, List<Place> places) {
        List<LinkedPlace> list = places.stream().map(LinkedPlace::new)
                .collect(Collectors.toList());

        // Linked Image Mapper
        if (call.queryBool("linked.image")) {
            int size = call.queryInt("linked.image.size");
            fillImage(list, size);
        }

        ArrayNode nodes = objectMapper.createArrayNode();
        list.forEach(place -> nodes.add(place.toNode()));
        return nodes;
    }

    /**
     * @param list list of linked place
     * @param size size of images
     */
    private void fillImage(List<LinkedPlace> list, int size) {
        List<String> keys = list.stream().map(LinkedPlace::getId).collect(Collectors.toList());
        Map<String, List<ImageLink>> map = imageDatabase.resolve(keys, size);
        list.forEach(place -> place.images = map.get(place.getId()));
    }

    /**
     * Linked place for holding place and it's linked data
     */
    private class LinkedPlace {
        private Place place;
        private List<ImageLink> images;

        private LinkedPlace(Place place) {
            this.place = place;
        }

        private String getId() {
            return place.getId();
        }

        /**
         * @return place to json node
         */
        public JsonNode toNode() {
            ObjectNode linked = objectMapper.createObjectNode();
            linked.set("images", objectMapper.valueToTree(images));

            ObjectNode node = objectMapper.valueToTree(place);
            node.set("linked", linked);
            return node;
        }
    }
}
