package munch.api.endpoints.service;

import com.typesafe.config.ConfigFactory;
import munch.restful.client.RestfulClient;

import javax.inject.Singleton;
import java.util.Optional;

/**
 * Created by: Fuxing
 * Date: 13/4/2017
 * Time: 2:11 AM
 * Project: munch-core
 */
@Singleton
public class NominatimService extends RestfulClient {

    public NominatimService() {
        super(ConfigFactory.load().getString("services.nominatim.url"));
    }

    public Optional<String> reverseGeocode(double lat, double lng, int zoom) {
        String suburb = doGet("reverse")
                .queryString("format", "json")
                .queryString("lat", lat)
                .queryString("lng", lng)
                .queryString("zoom", zoom)
                .hasMetaCodes(200)
                .getNode()
                .path("address").path("suburb").asText(null);

        // Only reverse geocode if suburb exist
        // Station
        // Suburb
        // Neighhourhood?
        return Optional.ofNullable(suburb);
    }
}
