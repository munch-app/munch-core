package munch.api.endpoints.service;

import com.typesafe.config.Config;
import munch.restful.client.RestfulClient;
import munch.restful.client.exception.StructuredException;
import munch.struct.neighborhood.Neighborhood;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 14/4/2017
 * Time: 11:26 PM
 * Project: munch-core
 */
@Singleton
public class GeocoderClient extends RestfulClient {

    /**
     * Look at Geocoder service package to api service settings
     *
     * @param config config to load geocoder.url
     */
    @Inject
    public GeocoderClient(@Named("services") Config config) {
        super(config.getString("geocoder.url"));
    }

    public Neighborhood reverse(double lat, double lng) throws StructuredException {
        return doGet("/reverse")
                .queryString("lat", lat)
                .queryString("lng", lng)
                .hasMetaCodes(200, 404)
                .asDataObject(Neighborhood.class);
    }

    public Neighborhood geocode(String text) throws StructuredException {
        return doGet("/geocode")
                .queryString("text", text)
                .hasMetaCodes(200, 404)
                .asDataObject(Neighborhood.class);
    }

    public List<Neighborhood> search(String text) throws StructuredException {
        return doGet("/search")
                .queryString("text", text)
                .hasMetaCodes(200)
                .asDataList(Neighborhood.class);
    }
}