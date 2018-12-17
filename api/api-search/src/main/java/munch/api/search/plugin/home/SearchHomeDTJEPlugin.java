package munch.api.search.plugin.home;

import catalyst.airtable.AirtableApi;
import catalyst.airtable.AirtableRecord;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import munch.api.search.SearchQuery;
import munch.api.search.cards.SearchHomeDTJECard;
import munch.api.search.plugin.SearchCardPlugin;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created by: Fuxing
 * Date: 17/12/18
 * Time: 4:05 AM
 * Project: munch-core
 */
@Singleton
public final class SearchHomeDTJEPlugin implements SearchCardPlugin {
    private static final Logger logger = LoggerFactory.getLogger(SearchHomeDTJEPlugin.class);

    private static final SearchHomeDTJECard DEFAULT;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    static {
        DEFAULT = new SearchHomeDTJECard(
                List.of("Chicken Rice", "Laksa", "Nasi Lemak", "Cai Fan", "Lor Mee"),
                List.of("Mee Rebus", "Duck Rice", "Chicken Chop", "Pad Thai", "Bak Chor Mee")
        );
    }

    private final AirtableApi.Table table;
    private final Cache<String, Optional<SearchHomeDTJECard>> cache;

    @Inject
    public SearchHomeDTJEPlugin(AirtableApi airtableApi) {
        this.table = airtableApi.base("appJ00hAzinOFaceE").table("DTJE");
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(2)
                .expireAfterWrite(2, TimeUnit.HOURS)
                .build();
    }

    @Nullable
    @Override
    public List<Position> load(Request request) {
        if (!request.isFirstPage()) return null;
        if (!request.getRequest().isFeature(SearchQuery.Feature.Home)) return null;

        LocalDateTime localTime = request.getRequest().getLocalTime();
        if (localTime == null) return null;

        return of(-8, load(localTime));
    }

    private SearchHomeDTJECard load(LocalDateTime dateTime) {
        String date = FORMATTER.format(dateTime);

        try {
            return cache.get(date, () -> Optional.ofNullable(get(date)))
                    .orElse(DEFAULT);
        } catch (Exception e) {
            logger.warn("DTJE Failed: {}", e);
        }

        return DEFAULT;
    }


    @Nullable
    private SearchHomeDTJECard get(String date) {
        String formula = "IS_SAME(Date,DATETIME_PARSE('" + date + "'))";

        List<AirtableRecord> records = table.filterByFormula(formula);
        if (records.size() != 2) return null;

        List<String> lunch = find("Lunch", records);
        List<String> dinner = find("Dinner", records);
        if (lunch == null || dinner == null) return null;

        return new SearchHomeDTJECard(lunch, dinner);
    }

    @Nullable
    private List<String> find(String hour, List<AirtableRecord> records) {
        for (AirtableRecord record : records) {
            if (StringUtils.equals(record.getFieldString("Hour"), hour)) {
                @NotNull List<String> list = record.getFieldList("List", String.class);
                if (list.size() < 5) return null;
                return list;
            }
        }

        return null;
    }
}
