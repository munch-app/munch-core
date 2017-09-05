package munch.api.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.DataClient;
import munch.api.services.places.PlaceCardGenerator;
import munch.data.Place;
import munch.data.places.PlaceCard;
import munch.restful.server.JsonCall;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 2/4/2017
 * Time: 4:15 PM
 * Project: munch-core
 */
@Singleton
public class PlaceService extends AbstractService {
    private final DataClient dataClient;
    private final PlaceCardGenerator cardGenerator;

    @Inject
    public PlaceService(DataClient dataClient, PlaceCardGenerator cardGenerator) {
        this.dataClient = dataClient;
        this.cardGenerator = cardGenerator;
    }

    /**
     * Endpoint: /v/places/*
     * Endpoint: /v/places/:placeId/*
     */
    @Override
    public void route() {
        // Single place endpoint
        PATH("/places/:placeId", () -> {
            GET("", this::get);
            GET("/cards", this::cards);

            PATH("/detailed", () -> {
                // Future in Beta
                // Instagram
                // Article
                // Reviews
            });
        });
    }

    /**
     * GET = /places/:placeId
     *
     * @param call json call
     * @return Place or Null
     */
    private Place get(JsonCall call) {
        String placeId = call.pathString("placeId");
        return dataClient.get(placeId);
    }

    /**
     * GET = /places/:placeId/cards
     *
     * @param call json call
     * @return List of Cards in Order
     */
    private List<PlaceCard> cards(JsonCall call) {
        String placeId = call.pathString("placeId");
        return cardGenerator.generate(placeId);
    }
}
