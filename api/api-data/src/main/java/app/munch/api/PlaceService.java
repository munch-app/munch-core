package app.munch.api;

import app.munch.model.Place;
import app.munch.model.PlaceRevision;
import app.munch.model.Profile;
import app.munch.v22v23.PlaceBridge;
import dev.fuxing.err.ForbiddenException;
import dev.fuxing.err.NotFoundException;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.service.TransportContext;
import munch.data.client.PlaceClient;

import javax.inject.Inject;
import java.sql.Timestamp;

/**
 * Created by: Fuxing
 * Date: 2019-08-12
 * Time: 05:46
 */
public final class PlaceService extends DataService {

    @Deprecated
    private final PlaceClient placeClient;
    private final PlaceBridge placeBridge;

    @Inject
    PlaceService(TransactionProvider provider, PlaceClient placeClient, PlaceBridge placeBridge) {
        super(provider);
        this.placeClient = placeClient;
        this.placeBridge = placeBridge;
    }

    @Override
    public void route() {
        PATH("/places/:placeId", () -> {
            PATH("/v22-v23", () -> {
                GET("", this::v22v23Get);
            });

            PATH("/revisions", () -> {
//                POST("", this::revisionPost);
            });
        });
    }

    public Place v22v23Get(TransportContext ctx) {
        // placeId is v22 type: UUID
        String deprecatedId = ctx.pathString("placeId");
        munch.data.place.Place deprecatedPlace = placeClient.get(deprecatedId);
        if (deprecatedPlace == null) {
            throw new NotFoundException();
        }

        return provider.reduce(entityManager -> {
            Place place = entityManager.createQuery("FROM Place " +
                    "WHERE deprecatedId = :deprecatedId", Place.class)
                    .getSingleResult();

            if (place == null) {
                place = new Place();
                place.setDeprecatedId(deprecatedId);
                place.setCreatedAt(new Timestamp(deprecatedPlace.getCreatedMillis()));
            }

            // Whenever queried it's automatically updated
            placeBridge.bridge(entityManager, place, deprecatedPlace);
            entityManager.persist(place);
            return place;
        });
    }

    public PlaceRevision revisionPost(TransportContext ctx) {
        String accountId = ctx.get(ApiRequest.class).getAccountId();
        String placeId = ctx.pathString("placeId");
        PlaceRevision revision = ctx.bodyAsObject(PlaceRevision.class);

        return provider.reduce(entityManager -> {
            Profile profile = entityManager.createQuery("SELECT profile FROM Account " +
                    "WHERE id = :accountId", Profile.class)
                    .setParameter("accountId", accountId)
                    .getSingleResult();

            if (profile == null) {
                throw new ForbiddenException();
            }

            // TODO(fuxing): whether new entry get automatically created
            Place place = entityManager.find(Place.class, placeId);
            if (place == null) throw new NotFoundException();

            revision.setPlace(place);
            revision.setCreatedBy(profile);

            entityManager.persist(revision);
            return revision;
        });
    }
}
