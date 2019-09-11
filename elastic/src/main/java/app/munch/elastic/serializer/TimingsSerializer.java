package app.munch.elastic.serializer;

import app.munch.model.Day;
import app.munch.model.ElasticDocument;
import app.munch.model.Hour;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 10/9/19
 * Time: 3:59 pm
 */
public interface TimingsSerializer {

    @Nullable
    default ElasticDocument.Timings serializeTimings(Collection<Hour> hours) {
        if (hours == null) return null;
        if (hours.isEmpty()) return null;

        ElasticDocument.Timings timings = new ElasticDocument.Timings();
        timings.setMon(collectDay(Day.MON, hours));
        timings.setTue(collectDay(Day.TUE, hours));
        timings.setWed(collectDay(Day.WED, hours));
        timings.setThu(collectDay(Day.THU, hours));
        timings.setFri(collectDay(Day.FRI, hours));
        timings.setSat(collectDay(Day.SAT, hours));
        timings.setSun(collectDay(Day.SUN, hours));
        return timings;
    }

    private Set<ElasticDocument.Timings.Range> collectDay(Day day, Collection<Hour> hours) {
        return hours.stream()
                .filter(hour -> hour.getDay().equals(day))
                .map(hour -> {
                    ElasticDocument.Timings.Range range = new ElasticDocument.Timings.Range();
                    range.setGte(hour.getOpen());
                    range.setLte(hour.getClose());
                    return range;
                })
                .collect(Collectors.toSet());
    }

}
