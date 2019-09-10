package app.munch.api.migration;

import app.munch.migration.PlaceMigration;
import app.munch.model.Place;
import app.munch.model.PlaceImage;
import dev.fuxing.err.NotFoundException;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.transport.service.TransportService;
import munch.data.client.PlaceClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

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

    private final TransactionProvider provider;

    private final PlaceMigration placeMigrationManager;

    @Inject
    PlaceMigrationService(PlaceClient placeClient, TransactionProvider provider, PlaceMigration placeMigrationManager) {
        this.placeClient = placeClient;
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

        return placeMigrationManager.map(deprecatedPlace);
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
