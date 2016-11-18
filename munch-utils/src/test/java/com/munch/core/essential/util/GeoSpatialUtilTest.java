package com.munch.core.essential.util;

import org.fest.assertions.Delta;
import org.junit.jupiter.api.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.distance.DistanceUtils;
import org.locationtech.spatial4j.shape.Point;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by: Fuxing
 * Date: 19/11/2016
 * Time: 2:03 AM
 * Project: munch-core
 */
public class GeoSpatialUtilTest {

    @Test
    public void distanceBetweenARoom() {
        Point point = SpatialContext.GEO.getShapeFactory().pointXY(103.851243, 1.348773);
        double doc1DistDEG = SpatialContext.GEO.calcDistance(point, 103.851359, 1.348750);
        double distance = DistanceUtils.degrees2Dist(doc1DistDEG, DistanceUtils.EARTH_MEAN_RADIUS_KM);

        assertThat(distance).isEqualTo(0.013, Delta.delta(0.001));

        double doc2DistDEG = SpatialContext.GEO.calcDistance(SpatialContext.GEO.getShapeFactory().pointXY(103.851243, 1.348776), 103.851215, 1.348786);
        double distance1 = DistanceUtils.degrees2Dist(doc2DistDEG, DistanceUtils.EARTH_MEAN_RADIUS_KM);

        assertThat(distance1).isLessThan(0.5);
    }

    @Test
    public void distanceBetweenARoomWithUtil() {
        Point point = GeoSpatialUtil.makePoint(1.348773, 103.851243);
        double distance = GeoSpatialUtil.calcDistance(point, 1.348750, 103.851359);
        assertThat(distance).isEqualTo(0.013, Delta.delta(0.001));

        double distance1 = GeoSpatialUtil.calcDistance(1.348776, 103.851243, 1.348786, 103.851215);
        assertThat(distance1).isLessThan(0.5);
    }

}