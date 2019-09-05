package app.munch.worker;

import app.munch.utils.website.WebsiteBrowser;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.utils.JsonUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.time.Duration;

/**
 * Created by: Fuxing
 * Date: 4/9/19
 * Time: 10:25 am
 */
@Singleton
public final class LinkedDataFetcher {
    private final WebsiteBrowser bookBrowser;
    private final WebsiteBrowser wwwBrowser;

    @Inject
    LinkedDataFetcher() {
        this.bookBrowser = new WebsiteBrowser("https://book.chope.co");
        this.wwwBrowser = new WebsiteBrowser("https://www.chope.co");
    }

    /**
     * Fetch from book.chope.co then deep fetch from www.chope.co
     */
    public JsonNode fetch(String rid) {
        try {
            Document document = bookBrowser.open("/booking?rid=" + rid, 3, Duration.ofMinutes(10));
            for (Element anchor : document.select(".logo_box .content a")) {
                String href = anchor.absUrl("href");

                JsonNode ld = deepFetch(href);
                if (ld != null) return ld;
            }

            throw new RuntimeException("Place website not found.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Fetch from www.chope.co
     */
    @Nullable
    private JsonNode deepFetch(String href) throws IOException {
        Document document = wwwBrowser.open(href, 3, Duration.ofMinutes(10));
        Elements scripts = document.select("script[type=application/ld+json]");

        for (Element script : scripts) {
            String data = sanitize(script.data());
            JsonNode node = JsonUtils.jsonToTree(data);
            if (validate(node)) return node;
        }

        return null;
    }

    /**
     * Some known error in JSON Application made by programmers
     */
    private String sanitize(String data) {
        if (data == null) return null;
        data = data.replace("\n", "");
        data = data.replace("\r", "");
        data = data.replace("\t", "");
        return data;
    }

    /**
     * Validate it's the correct application/json+ld data
     */
    private boolean validate(JsonNode node) {
        if (!node.path("@type").asText().equals("Restaurant")) return false;
        if (!node.path("@context").asText().equals("http://schema.org")) return false;

        return true;
    }
}
