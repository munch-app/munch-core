package munch.api.endpoints.service;

import com.google.inject.Singleton;
import munch.api.PlaceRandom;
import munch.document.DocumentQuery;
import munch.search.SearchQuery;
import munch.struct.Place;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 3:18 AM
 * Project: munch-core
 */
@Singleton
public class SearchService {

    private final SearchQuery search;
    private final DocumentQuery document;

    private final PlaceRandom placeRandom;

    @Inject
    public SearchService(SearchQuery search, DocumentQuery document, PlaceRandom placeRandom) {
        this.search = search;
        this.document = document;
        this.placeRandom = placeRandom;
    }

    // TODO: No location search
    // TODO: Explicit filters

    public List<Place> search(double lat, double lng) {
        return null;
    }

    public List<Place> search(double lat, double lng, String query) {
        return null;
    }

    public List<Place> search(String query) {
        return null;
    }
}
