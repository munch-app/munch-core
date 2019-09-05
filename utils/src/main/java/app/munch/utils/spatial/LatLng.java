package app.munch.utils.spatial;


import org.apache.commons.lang3.tuple.Pair;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 8/7/2017
 * Time: 12:58 AM
 * Project: corpus-catalyst
 */
public final class LatLng extends Pair<Double, Double> {
    private final double lat;
    private final double lng;

    public LatLng(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    @Override
    public Double getLeft() {
        return lat;
    }

    @Override
    public Double getRight() {
        return lng;
    }

    @Override
    public Double setValue(Double value) {
        return null;
    }

    /**
     * @return convert to "lat,lng"
     */
    @Override
    public String toString() {
        return lat + "," + lng;
    }

    /**
     * @param latLng other latLng
     * @return distance in meters
     */
    public double distance(LatLng latLng) {
        return distance(latLng.getLat(), latLng.getLng());
    }

    /**
     * @param lat other latitude
     * @param lng other longitude
     * @return distance in meters
     */
    public double distance(double lat, double lng) {
        return distance(getLat(), getLng(), lat, lng);
    }

    /**
     * @param latLng latLng to check
     * @return true is it is latLng, false if its not
     */
    public static boolean isValid(@NotNull String latLng) {
        try {
            parse(latLng);
            return true;
        } catch (ParseException pe) {
            return false;
        }
    }

    /**
     * Parse latitude longitude from string to LatLng Struct
     *
     * @param latLng latitude longitude in string format "lat,lng"
     * @return LatLng or null if blank
     * @throws ParseException if failed to parse
     */
    public static LatLng parse(String latLng) throws ParseException {
        Objects.requireNonNull(latLng);

        try {
            String[] split = latLng.split(",");
            double lat = Double.parseDouble(split[0].trim());
            double lng = Double.parseDouble(split[1].trim());
            return new LatLng(lat, lng);
        } catch (NullPointerException | IndexOutOfBoundsException | NumberFormatException e) {
            throw new ParseException(e);
        }
    }

    /**
     * @param latLng latitude and longitude
     * @return "lat,lng"
     */
    public static String toString(LatLng latLng) {
        return latLng.toString();
    }

    /**
     * @param lat latitude
     * @param lng longitude
     * @return "lat,lng"
     */
    public static String toString(double lat, double lng) {
        return lat + "," + lng;
    }

    /**
     * @return Distance in meters
     */
    public static double distance(String latLng1, String latLng2) {
        return parse(latLng1).distance(parse(latLng2));
    }

    /**
     * Calculate distance between two points in latitude and longitude
     * <p>
     * lat1, lon1 Start point lat2, lon2
     *
     * @return Distance in meters
     */
    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        return distance(lat1, lon1, 0, lat2, lon2, 0);
    }

    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     * <p>
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     *
     * @return Distance in meters
     */
    public static double distance(double lat1, double lon1, double el1,
                                  double lat2, double lon2, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }


    /**
     * Exception wrapper for parsing latLng
     */
    public static class ParseException extends RuntimeException {
        private ParseException(RuntimeException e) {
            super(e);
        }
    }
}
