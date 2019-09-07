package app.munch.worker;

import app.munch.model.Affiliate;
import app.munch.model.AffiliateBrand;
import app.munch.model.AffiliateType;
import app.munch.model.PlaceStruct;
import app.munch.worker.google.GoogleSheetService;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Iterators;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Created by: Fuxing
 * Date: 3/9/19
 * Time: 6:59 pm
 */
@Singleton
public final class ChopeAffiliateFetcher {
    public static final String SOURCE = "book.chope.co";
    private static final AffiliateBrand AFFILIATE_BRAND;

    static {
        AFFILIATE_BRAND = new AffiliateBrand();
        AFFILIATE_BRAND.setUid("01dkvfmtapgz4jgqjy4gz67417");
    }

    private static final Base64.Encoder encoder = Base64.getEncoder();

    private final GoogleSheetService googleSheetService;
    private final LinkedDataFetcher linkedDataFetcher;
    private final PlaceStructParser placeStructParser;

    @Inject
    ChopeAffiliateFetcher(GoogleSheetService googleSheetService, LinkedDataFetcher linkedDataFetcher, PlaceStructParser placeStructParser) {
        this.googleSheetService = googleSheetService;
        this.linkedDataFetcher = linkedDataFetcher;
        this.placeStructParser = placeStructParser;
    }

    public Iterator<Affiliate> fetch() throws IOException {
        Iterator<List<Object>> sheetIterator = googleSheetService.iterator();

        Iterator<Affiliate> iterator = Iterators.transform(sheetIterator, this::mapAffiliate);
        iterator = Iterators.filter(iterator, Objects::nonNull);
        return iterator;
    }

    @Nullable
    private Affiliate mapAffiliate(List<Object> row) {
        if (row.size() < 3) return null;

        String name = row.get(0).toString();
        String baseReferralUrl = row.get(1).toString();
        String munchReferralUrl = row.get(2).toString();

        if (StringUtils.isAnyBlank(name, baseReferralUrl, munchReferralUrl)) return null;
        if (!baseReferralUrl.startsWith("http")) return null;
        if (!munchReferralUrl.startsWith("http")) return null;

        String rid = parseRid(baseReferralUrl);
        if (rid == null) return null;

        return fetchAffiliate(rid);
    }

    private Affiliate fetchAffiliate(String rid) {
        Affiliate affiliate = new Affiliate();
        affiliate.setSourceKey(SOURCE, encoder.encodeToString(rid.getBytes()));
        affiliate.setType(AffiliateType.RESERVATION);
        affiliate.setUrl(buildUrl(rid));

        affiliate.setBrand(AFFILIATE_BRAND);

        JsonNode json = linkedDataFetcher.fetch(rid);
        PlaceStruct struct = placeStructParser.parse(json);
        affiliate.setPlaceStruct(struct);
        return affiliate;
    }

    private static String buildUrl(String rid) {
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

    private static String parseRid(String url) {
        List<NameValuePair> list = null;
        try {
            list = URLEncodedUtils.parse(new URI(url), StandardCharsets.UTF_8);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        for (NameValuePair pair : list) {
            if (pair.getName().equals("rid")) {
                return pair.getValue();
            }
        }

        return null;
    }

}
