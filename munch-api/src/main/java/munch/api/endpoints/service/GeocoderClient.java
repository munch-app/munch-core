package munch.api.endpoints.service;

import com.typesafe.config.Config;
import munch.restful.client.RestfulClient;
import munch.restful.client.exception.StructuredException;
import org.wololo.geojson.GeoJSON;

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
    public GeocoderClient(@Named("services") Config config) {
        super(config.getString("geocoder.url"));
    }

    public Place reverse(double lat, double lng) throws StructuredException {
        return doGet("/reverse")
                .queryString("lat", lat)
                .queryString("lng", lng)
                .hasMetaCodes(200)
                .asDataObject(Place.class);
    }

    public Place geocode(String text) throws StructuredException {
        return doGet("/geocode")
                .queryString("text", text)
                .hasMetaCodes(200)
                .asDataObject(Place.class);
    }

    public List<Place> search(String text) throws StructuredException {
        return doGet("/search")
                .queryString("text", text)
                .hasMetaCodes(200)
                .asDataList(Place.class);
    }

    /**
     * Geocoder service place object
     * Lat, lng is the center is currently optional
     */
    public static class Place {
        private String name;
        private Double lat;
        private Double lng;
        private GeoJSON geo;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

        public Double getLng() {
            return lng;
        }

        public void setLng(Double lng) {
            this.lng = lng;
        }

        public GeoJSON getGeo() {
            return geo;
        }

        public void setGeo(GeoJSON geo) {
            this.geo = geo;
        }
    }
}
