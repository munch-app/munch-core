/*
 * Copyright (c) 2015. This file is part of 3LINES PTE. LTD. property that is used for project Puffin which is not to be released for personal use.
 * Failing to do so will result in heavy penalty. However, if this individual file might need to be taken out of context for other purposes within the company,
 * please do ask for permission.
 */

package com.munch.core.essential.util;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.distance.DistanceUtils;
import org.locationtech.spatial4j.shape.Point;

/**
 * Distance util in km (4326 projection)
 * <p>
 * Created by Fuxing
 * Date: 14/2/2015
 * Time: 9:22 PM
 * Project: PuffinCore
 */
public class GeoSpatialUtil {
    private GeoSpatialUtil() {
    }

    /**
     * Make a standard Geo Spatial com.spatial4j.core.Point
     *
     * @param lat latitude of location
     * @param lng longitude of location
     * @return Point
     */
    public static Point makePoint(double lat, double lng) {
        return SpatialContext.GEO.getShapeFactory().pointXY(lng, lat);
    }

    /**
     * Calculate distance between 2 point
     *
     * @param point  point a
     * @param point1 point b
     * @return distance apart in km
     */
    public static double calcDistance(Point point, Point point1) {
        return DistanceUtils.degrees2Dist(SpatialContext.GEO.calcDistance(point, point1), DistanceUtils.EARTH_MEAN_RADIUS_KM);
    }

    /**
     * Calculate distance between 2 point
     *
     * @param point point a
     * @param lat   point b latitude
     * @param lng   point b longitude
     * @return distance apart in km
     */
    public static double calcDistance(Point point, double lat, double lng) {
        return DistanceUtils.degrees2Dist(SpatialContext.GEO.calcDistance(point, lng, lat), DistanceUtils.EARTH_MEAN_RADIUS_KM);
    }

    /**
     * Calculate distance between 2 lat/lng
     *
     * @param lat1 point a latitude
     * @param lng1 point a longitude
     * @param lat2 point b latitude
     * @param lng2 point b longitude
     * @return distance apart in km
     */
    public static double calcDistance(double lat1, double lng1, double lat2, double lng2) {
        return calcDistance(makePoint(lat1, lng1), lat2, lng2);
    }

    // TODO, in the future add a faster function that don't do so much match, used for pure comparing
}
