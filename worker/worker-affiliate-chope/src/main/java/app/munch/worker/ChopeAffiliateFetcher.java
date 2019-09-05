package app.munch.worker;

import app.munch.model.Affiliate;
import app.munch.model.AffiliateBrand;
import app.munch.model.AffiliateType;
import app.munch.model.PlaceStruct;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Iterators;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
    private static final String CSV_URL = "https://docs.google.com/spreadsheets/d/1vNNoveTpyiOaMEE2B16D7rKcG4ZIacTmXPQCbxovDvg/export?format=csv&id=1vNNoveTpyiOaMEE2B16D7rKcG4ZIacTmXPQCbxovDvg&gid=0";

    private final LinkedDataFetcher linkedDataFetcher;
    private final PlaceStructParser placeStructParser;

    @Inject
    ChopeAffiliateFetcher(LinkedDataFetcher linkedDataFetcher, PlaceStructParser placeStructParser) {
        this.linkedDataFetcher = linkedDataFetcher;
        this.placeStructParser = placeStructParser;
    }

    public Iterator<Affiliate> fetch() throws IOException {
        List<List<String>> rowList = getRowList();

        Iterator<Affiliate> iterator = Iterators.transform(rowList.iterator(), this::mapAffiliate);
        iterator = Iterators.filter(iterator, Objects::nonNull);
        return iterator;
    }

    private List<List<String>> getRowList() throws IOException {
        try (ICsvListReader csvReader = getCsvReader()) {
            List<List<String>> rowList = new ArrayList<>();

            List<String> row;
            while ((row = csvReader.read()) != null) {
                rowList.add(row);
            }

            return rowList;
        }
    }

    private ICsvListReader getCsvReader() throws IOException {
        File file = File.createTempFile(RandomStringUtils.randomAlphanumeric(20), ".tmp");
        FileUtils.copyURLToFile(new URL(CSV_URL), file);

        CsvListReader listReader = new CsvListReader(new FileReader(file), CsvPreference.STANDARD_PREFERENCE);

        // First line is invalid
        listReader.getHeader(true);
        return listReader;
    }

    @Nullable
    private Affiliate mapAffiliate(List<String> row) {
        if (row.size() < 3) return null;

        String name = row.get(0);
        String baseReferralUrl = row.get(1);
        String munchReferralUrl = row.get(2);

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
