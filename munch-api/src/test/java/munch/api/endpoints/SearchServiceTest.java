package munch.api.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

/**
 * Created By: Fuxing Loh
 * Date: 22/3/2017
 * Time: 9:32 PM
 * Project: munch-core
 */
class SearchServiceTest extends AbstractEndpointTest {

    public SearchServiceTest() {
        super(SearchService.class, Elastic.get(), Postgres.get());
    }

    @Test
    void discover() throws Exception {
        ObjectNode request = mapper.createObjectNode();
        request.set("spatial", spatialNode(1.350987, 103.848655));

        JsonNode response = doPost("/discover")
                .body(request)
                .asNode();

        System.out.println(response);
    }
}