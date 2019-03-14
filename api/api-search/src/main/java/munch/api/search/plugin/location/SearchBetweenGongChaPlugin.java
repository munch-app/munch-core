package munch.api.search.plugin.location;

import munch.api.search.SearchQuery;
import munch.api.search.cards.SearchClaimVoucherCard;
import munch.api.search.plugin.SearchCardPlugin;
import munch.file.Image;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 2019-03-11
 * Time: 03:54
 * Project: munch-core
 */
@Singleton
public final class SearchBetweenGongChaPlugin implements SearchCardPlugin {
    private final SearchClaimVoucherCard card;

    @Inject
    public SearchBetweenGongChaPlugin() {
        Image image = new Image();
        image.setImageId("Campaign_GongChaCard");
        image.setSizes(List.of(
                newSize("https://s3-ap-southeast-1.amazonaws.com/munch-static/Campaign/GongChaCard%404x.png", 1280, 800),
                newSize("https://s3-ap-southeast-1.amazonaws.com/munch-static/Campaign/GongChaCard%403x.png", 960, 600),
                newSize("https://s3-ap-southeast-1.amazonaws.com/munch-static/Campaign/GongChaCard%402x.png", 640, 400)
        ));

        this.card = new SearchClaimVoucherCard(
                "00000000-0000-0000-0000-000000000001",
                image,
                "You found the spot for Jane and John!",
                "TAP TO REDEEM"
        );
    }

    @Nullable
    @Override
    public List<Position> load(Request request) {
        if (!request.getRequest().isBetween()) return null;
        if (!request.isFirstPage()) return null;

        LocalDateTime dateTime = getDateTime(request);
        if (dateTime.isBefore(LocalDateTime.of(2019, Month.MARCH, 17, 9, 0))) {
            return null;
        }
        if (dateTime.isAfter(LocalDateTime.of(2019, Month.MARCH, 23, 22, 0))) {
            return null;
        }

        List<SearchQuery.Filter.Location.Point> points = request.getRequest().getPoints();
        if (points.size() != 2) return null;

        Set<String> names = points.stream()
                .map(point -> StringUtils.lowerCase(point.getName()))
                .collect(Collectors.toSet());

        // allow MRT?
        if (hasNames(names)) {
            return of(0, card);
        }

        return List.of();
    }

    public LocalDateTime getDateTime(Request request) {
        LocalDateTime dateTime = request.getRequest().getLocalTime();
        if (dateTime != null) return dateTime;

        return LocalDateTime.now(ZoneId.of("Asia/Singapore"));
    }

    private static Image.Size newSize(String url, int width, int height) {
        Image.Size size = new Image.Size();
        size.setUrl(url);
        size.setWidth(width);
        size.setHeight(height);
        return size;
    }

    private static boolean hasNames(Set<String> names) {
        boolean kallang = names.contains("kallang") || names.contains("kallang mrt");
        boolean tiong = names.contains("tiong bahru") || names.contains("tiong bahru mrt");
        return kallang && tiong;
    }
}
