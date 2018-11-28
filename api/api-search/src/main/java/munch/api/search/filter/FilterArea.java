package munch.api.search.filter;

/**
 * Truncated version of munch.data.location.Area
 *
 * Created by: Fuxing
 * Date: 28/11/18
 * Time: 4:40 AM
 * Project: munch-core
 */
public final class FilterArea {
    public String areaId;
    public String name;

    public FilterArea(String areaId, String name) {
        this.areaId = areaId;
        this.name = name;
    }
}
