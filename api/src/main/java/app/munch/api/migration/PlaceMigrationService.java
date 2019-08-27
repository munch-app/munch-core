package app.munch.api.migration;

import app.munch.model.Place;
import app.munch.model.PlaceImage;
import dev.fuxing.err.NotFoundException;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.transport.service.TransportService;
import munch.data.client.PlaceClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * Created by: Fuxing
 * Date: 27/8/19
 * Time: 10:28 PM
 * Project: munch-core
 */
@Singleton
public final class PlaceMigrationService implements TransportService {

    @Deprecated
    private final PlaceClient placeClient;
    private final PlaceBridge placeBridge;

    private final TransactionProvider provider;

    private final PlaceMigrationManager placeMigrationManager;

    @Inject
    PlaceMigrationService(PlaceClient placeClient, PlaceBridge placeBridge, TransactionProvider provider, PlaceMigrationManager placeMigrationManager) {
        this.placeClient = placeClient;
        this.placeBridge = placeBridge;
        this.provider = provider;
        this.placeMigrationManager = placeMigrationManager;
    }

    @Override
    public void route() {
        PATH("/migrations/places/:id", () -> {
            GET("", this::get);
            GET("/images", this::getImages);
        });
    }

    public Place get(TransportContext ctx) {
        String id = ctx.pathString("id");
        if (id.length() == 13) {
            return getByL13(id);
        } else {
            return getByUUID(id);
        }
    }

    public Place getByUUID(String cid) {
        munch.data.place.Place deprecatedPlace = placeClient.get(cid);
        if (deprecatedPlace == null) {
            throw new NotFoundException();
        }

        return provider.reduce(entityManager -> {
            Place place = Optional.of(entityManager.createQuery("FROM Place " +
                    "WHERE cid = :cid", Place.class)
                    .setParameter("cid", cid)
                    .getResultList())
                    .filter(places -> !places.isEmpty())
                    .map(places -> places.get(0))
                    .orElseGet(() -> {
                        Place newPlace = new Place();
                        newPlace.setCid(cid);
                        newPlace.setCreatedAt(new Timestamp(deprecatedPlace.getCreatedMillis()));
                        return newPlace;
                    });

            // Whenever queried it's automatically updated
            placeBridge.bridge(entityManager, place, deprecatedPlace);
            entityManager.persist(place);
            return place;
        });
    }

    public Place getByL13(String id) {
        return provider.reduce(entityManager -> {
            return entityManager.find(Place.class, id);
        });
    }

    public List<PlaceImage> getImages(TransportContext ctx) {
        String id = ctx.pathString("id");
        return placeMigrationManager.getPlaceImages(id);
    }
}
