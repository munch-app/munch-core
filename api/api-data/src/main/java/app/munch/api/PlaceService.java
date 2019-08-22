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
import java.util.Optional;

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
            GET("/v22-v23", this::v22v23Get);
            GET("/v23", this::v23Get);

            PATH("/revisions", () -> {
//                POST("", this::revisionPost);
            });
        });
    }

    public Place v22v23Get(TransportContext ctx) {
        // placeId is v22 type: UUID
        String cid = ctx.pathString("placeId");
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

    /**
     * For URL Redirecting
     */
    public Place v23Get(TransportContext ctx) {
        String id = ctx.pathString("placeId");

        return provider.reduce(entityManager -> {
            return entityManager.find(Place.class, id);
        });
    }

    public PlaceRevision revisionPost(TransportContext ctx) {
        String accountId = ctx.get(ApiRequest.class).getAccountId();
        String placeId = ctx.pathString("placeId");
        PlaceRevision revision = ctx.bodyAsObject(PlaceRevision.class);

        return provider.reduce(entityManager -> {
            Profile profile = entityManager.createQuery("SELECT a.profile FROM Account a " +
                    "WHERE a.id = :accountId", Profile.class)
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