package munch.api.endpoints.service;

import com.typesafe.config.Config;
import munch.restful.client.RestfulClient;

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
public class GeocoderService extends RestfulClient {

    public GeocoderService(@Named("services") Config config) {
        super(config.getString("geocoder.url"));
    }

    public Place reverse(double lat, double lng) {
        doGet("/geocode/reverse")
                .queryString("lat", lat)
                .queryString("lng", lng)
                .asDataNode();
    }

    public Place geocode(String text) {

    }

    public List<Place> search(String text) {

    }

    public static class Place {
        private String name;
        private String WKT;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getWKT() {
            return WKT;
        }

        public void setWKT(String WKT) {
            this.WKT = WKT;
        }
    }
}
