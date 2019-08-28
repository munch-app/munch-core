package app.munch.api;

import app.munch.model.Place;
import app.munch.model.PlaceRevision;
import app.munch.model.Profile;
import dev.fuxing.err.ForbiddenException;
import dev.fuxing.err.NotFoundException;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.service.TransportContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 2019-08-12
 * Time: 05:46
 */
@Singleton
public final class PlaceService extends DataService {

    @Inject
    PlaceService(TransactionProvider provider) {
        super(provider);
    }

    @Override
    public void route() {
        PATH("/places/:id", () -> {
            PATH("/revisions", () -> {
//                POST("", this::revisionPost);
            });
        });
    }

    public PlaceRevision revisionPost(TransportContext ctx) {
        String id = ctx.pathString("id");
        String accountId = ctx.get(ApiRequest.class).getAccountId();
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
            Place place = entityManager.find(Place.class, id);
            if (place == null) throw new NotFoundException();

            revision.setPlace(place);
            revision.setCreatedBy(profile);

            entityManager.persist(revision);
            return revision;
        });
    }
}
