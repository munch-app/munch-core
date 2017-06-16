package munch.api.clients;

import com.typesafe.config.Config;
import munch.api.data.Location;
import munch.restful.client.RestfulClient;
import munch.restful.client.exception.StructuredException;

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
public class LocationClient extends RestfulClient {

    @Inject
    public LocationClient(@Named("services") Config config) {
        super(config.getString("geocoder.url"));
    }

    public Location reverse(double lat, double lng) throws StructuredException {
        return doGet("/reverse")
                .queryString("lat", lat)
                .queryString("lng", lng)
                .hasMetaCodes(200, 404)
                .asDataObject(Location.class);
    }

    public Location geocode(String text) throws StructuredException {
        return doGet("/geocode")
                .queryString("text", text)
                .hasMetaCodes(200, 404)
                .asDataObject(Location.class);
    }

    public List<Location> search(String text) throws StructuredException {
        return doGet("/search")
                .queryString("text", text)
                .hasMetaCodes(200)
                .asDataList(Location.class);
    }
}