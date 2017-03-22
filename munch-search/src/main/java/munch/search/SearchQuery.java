package munch.search;

import com.fasterxml.jackson.databind.JsonNode;
import munch.struct.Place;

import java.io.IOException;
import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 22/3/2017
 * Time: 9:22 PM
 * Project: munch-core
 */
public interface SearchQuery {

    List<Place> query(JsonNode node) throws IOException;

}
