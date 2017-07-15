package munch.api.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpMethod;
import munch.api.data.Location;
import munch.restful.client.RestfulRequest;
import org.junit.jupiter.api.Test;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by: Fuxing
 * Date: 15/7/2017
 * Time: 4:AM
 * Project: munch-core
 */
class StaticJsonTest {

    @Test
    void generatePopularPlaces() throws Exception {
        String[] places = ("Orchard,Marina Bay,Clarke Quay,Bukit Timah,Chinatown," +
                "Downtown Core,Holland Village,Dhoby Ghaut,Little India,Tanjong Pagar," +
                "Changi,Bugis,Bedok,Jurong East,Serangoon").split(",");
        List<Location> locations = Arrays.stream(places)
                .map(this::query)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        assertThat(places.length).isEqualTo(locations.size());
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(locations));
    }

    @Nullable
    public Location query(String name) {
        RestfulRequest request = new RestfulRequest(HttpMethod.GET, "http://localhost:8800/v1/locations/suggest")
                .queryString("text", name);
        List<Location> locations = request.asResponse().asDataList(Location.class);
        if (locations.isEmpty()) return null;
        return locations.get(0);
    }
}