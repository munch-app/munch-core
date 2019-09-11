package app.munch.worker;

import app.munch.model.Affiliate;
import app.munch.model.AffiliateBrand;
import app.munch.model.AffiliateType;
import app.munch.model.PlaceStruct;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.err.NotFoundException;
import dev.fuxing.utils.JsonUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URISyntaxException;
import java.util.Base64;

/**
 * Created by: Fuxing
 * Date: 12/9/19
 * Time: 12:25 am
 */
@Singleton
public final class ChopeAffiliateParser {
    public static final String SOURCE = "book.chope.co";

    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final AffiliateBrand AFFILIATE_BRAND;

    static {
        AFFILIATE_BRAND = new AffiliateBrand();
        AFFILIATE_BRAND.setUid("01dkvfmtapgz4jgqjy4gz67417");
    }

    private final PlaceStruct.BuilderFactory builderFactory;

    @Inject
    ChopeAffiliateParser(PlaceStruct.BuilderFactory builderFactory) {
        this.builderFactory = builderFactory;
    }

    public Affiliate parse(Document document) {
        JsonNode node = LinkedData.find(document);
        String rid = RID.findRid(document);
        PlaceStruct struct = parseStruct(node);

        Affiliate affiliate = new Affiliate();
        affiliate.setSourceKey(SOURCE, encoder.encodeToString(rid.getBytes()));
        affiliate.setType(AffiliateType.RESERVATION);
        affiliate.setUrl(Linking.build(rid));

        affiliate.setBrand(AFFILIATE_BRAND);
        affiliate.setPlaceStruct(struct);
        return affiliate;
    }

    private PlaceStruct parseStruct(JsonNode node) {
        PlaceStruct.Builder builder = builderFactory.newBuilder();

        builder.name(node.path("name").asText());
        builder.address(node.path("address").path("streetAddress").asText());
        builder.postcode(node.path("address").path("postalCode").asText());

        JsonNode geo = node.path("geo");
        if (geo.has("latitude") && geo.has("longitude")) {
            builder.latLng(geo.path("latitude").asText() + "," + geo.path("longitude").asText());
        }

        builder.phone(node.path("telephone").asText());
        return builder.build();
    }

    public static class LinkedData {
        static JsonNode find(Document document) {
            Elements scripts = document.select("script[type=application/ld+json]");

            for (Element script : scripts) {
                String data = sanitize(script.data());
                JsonNode node = JsonUtils.jsonToTree(data);
                if (validate(node)) return node;
            }

            throw new NotFoundException("application/json+ld not found.");
        }

        /**
         * Some known error in JSON Application made by programmers
         */
        private static String sanitize(String data) {
            if (data == null) return null;
            data = data.replace("\n", "");
            data = data.replace("\r", "");
            data = data.replace("\t", "");
            return data;
        }

        /**
         * Validate it's the correct application/json+ld data
         */
        private static boolean validate(JsonNode node) {
            if (!node.path("@type").asText().equals("Restaurant")) return false;
            if (!node.path("@context").asText().equals("http://schema.org")) return false;

            return true;
        }
    }

    public static class RID {
        private static final String ACTION_URL = "https://bookv5.chope.co/booking/check";

        static String findRid(Document document) {
            for (Element element : document.select("form")) {
                if (!element.attr("action").equals(ACTION_URL)) continue;

                Elements inputs = element.select("input[name=rid]");
                if (inputs.size() != 1) {
                    throw new IllegalStateException("Cannot find rid.");
                }

                return inputs.attr("value");
            }

            throw new NotFoundException("rid not found.");
        }

    }

    public static class Linking {
        static String build(String rid) {
            URIBuilder builder = new URIBuilder();
            builder.setScheme("https");
            builder.setHost("book.chope.co");
            builder.setPath("booking");
            builder.addParameter("rid", rid);
            builder.addParameter("source", "munch");
            try {
                return builder.build().toString();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
