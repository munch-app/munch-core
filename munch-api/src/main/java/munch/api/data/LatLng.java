package munch.api.data;

/**
 * Special wrapper for HeaderL Location-LatLng
 */
public class LatLng {
    private final double lat;
    private final double lng;

    /**
     * @param latLng latLng is string separated by ,
     * @throws NumberFormatException     if number is not double
     * @throws IndexOutOfBoundsException if size is not 2
     * @throws NullPointerException      latlng is null, or either split string is null
     */
    public LatLng(String latLng) throws NullPointerException, IndexOutOfBoundsException, NumberFormatException {
        String[] split = latLng.split(",");
        lat = Double.parseDouble(split[0].trim());
        lng = Double.parseDouble(split[1].trim());
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    /**
     * @return "lat, lng"
     */
    public String getString() {
        return lat + "," + lng;
    }
}
