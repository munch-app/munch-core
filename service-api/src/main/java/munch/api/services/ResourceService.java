package munch.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.StaticJsonResource;
import spark.Spark;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 2/7/2017
 * Time: 3:16 AM
 * Project: munch-core
 */
@Singleton
public class ResourceService extends AbstractService {
    private static final SimpleDateFormat DateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");

    private final StaticJsonResource jsonResource;

    @Inject
    public ResourceService(StaticJsonResource jsonResource) throws IOException {
        this.jsonResource = jsonResource;
    }

    @Override
    public void route() {
        PATH("/resources", () -> {
            pathResources("/popular-locations", getResource("popular-locations"), new Date(1505473814000L));
        });
    }

    private void pathResources(String path, final String json, final Date lastModified) {
        PATH(path, () -> {
            Spark.head("", (request, response) -> {
                response.header("Last-Modified", DateFormat.format(lastModified));
                return "";
            });

            Spark.get("", (request, response) -> {
                response.header("Last-Modified", DateFormat.format(lastModified));
                return json;
            });
        });
    }

    private String getResource(String resourceName) {
        try {
            JsonNode resource = jsonResource.getResource(resourceName + ".json");
            return resource.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
