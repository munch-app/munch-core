package munch.api.services.cached;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.data.Location;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;

/**
 * Created by: Fuxing
 * Date: 15/7/2017
 * Time: 10:26 PM
 * Project: munch-core
 */
@Singleton
public final class CachedManager {
    private final StaticJsonResource resource;
    private final ObjectMapper objectMapper;

    private final Cached<Location[]> popularLocations;

    @Inject
    public CachedManager(StaticJsonResource resource, ObjectMapper objectMapper) throws IOException {
        this.resource = resource;
        this.objectMapper = objectMapper;

        // Eager load theses
        this.popularLocations = load("popular-locations.json", Location[].class);
    }


    /**
     * @return list of popular Locations
     */
    public Cached<Location[]> getPopularLocations() {
        return popularLocations;
    }

    private <T> Cached<T> load(String name, Class<T> clazz) throws IOException {
        JsonNode node = resource.getResource(name);
        String hash = DigestUtils.sha512Hex(node.toString());
        return new Cached<>(hash, objectMapper.treeToValue(node, clazz));
    }

    /**
     * @param <T> Data
     */
    public static class Cached<T> {
        private String hash;
        private T data;

        public Cached(String hash, T data) {
            this.hash = hash;
            this.data = data;
        }

        public String getHash() {
            return hash;
        }

        public T getData() {
            return data;
        }
    }
}
