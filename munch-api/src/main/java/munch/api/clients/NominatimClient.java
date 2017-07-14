package munch.api.clients;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import fr.dudie.nominatim.client.JsonNominatimClient;
import fr.dudie.nominatim.model.Address;
import fr.dudie.nominatim.model.Element;
import munch.api.data.LatLng;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Named;
import java.io.IOException;

/**
 * Created by: Fuxing
 * Date: 6/7/2017
 * Time: 5:14 AM
 * Project: munch-corpus
 */
@Singleton
public final class NominatimClient {
    private static final Logger logger = LoggerFactory.getLogger(NominatimClient.class);

    private final JsonNominatimClient jsonClient;

    @Inject
    public NominatimClient(@Named("services") Config config) {
        String url = config.getString("nominatim.url");
        HttpClient httpClient = HttpClientBuilder.create().build();
        this.jsonClient = new JsonNominatimClient(url, httpClient, "");
    }

    /**
     * @param lat lat of place
     * @param lng lng of place
     * @return Street Name if found
     */
    @Nullable
    public String getStreet(double lat, double lng) {
        try {
            Address address = jsonClient.getAddress(lng, lat);
            if (address == null) return null;

            Element[] elements = address.getAddressElements();
            if (elements == null || elements.length == 0) return null;

            // Find road value
            for (Element element : address.getAddressElements()) {
                if (element.getKey().equals("road")) return element.getValue();
            }

            return null;
        } catch (IOException ioe) {
            logger.error("Failed to reverse geocode", ioe);
            throw new RuntimeException(ioe);
        }
    }

    /**
     * @see NominatimClient#getStreet(double, double)
     */
    @Nullable
    public String getStreet(@Nullable LatLng latLng) {
        if (latLng == null) return null;
        return getStreet(latLng.getLat(), latLng.getLng());
    }
}
