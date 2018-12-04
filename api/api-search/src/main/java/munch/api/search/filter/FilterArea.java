package munch.api.search.filter;

import munch.data.location.Area;

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
    public Area.Type type;
    public String name;

    public FilterArea(String areaId, String name, Area.Type type) {
        this.areaId = areaId;
        this.name = name;
        this.type = type;
    }
}
