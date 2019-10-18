package app.munch.vision;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.ConfigFactory;
import dev.fuxing.utils.JsonUtils;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;

/**
 * @author Fuxing Loh
 * @since 2019-10-18 at 12:43 am
 */
@Singleton
public final class VisionClient {

    private final String url;

    @Inject
    VisionClient() {
        this.url = ConfigFactory.load().getString("services.vision.url");
    }

    public VisionResult post(File file) throws IOException {
        return Request.Post(url)
                .bodyFile(file, ContentType.APPLICATION_OCTET_STREAM)
                .execute()
                .handleResponse(VisionClient::handleResponse);
    }

    private static VisionResult handleResponse(HttpResponse response) throws IOException {
        StatusLine status = response.getStatusLine();
        String json = EntityUtils.toString(response.getEntity());
        if (status.getStatusCode() == 200) {
            JsonNode tree = JsonUtils.jsonToTree(json);
            return JsonUtils.toObject(tree.path("data"), VisionResult.class);
        }

        throw new RuntimeException(status.getStatusCode() + ": " + json);
    }
}
