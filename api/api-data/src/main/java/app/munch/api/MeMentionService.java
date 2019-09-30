package app.munch.api;

import app.munch.controller.MentionController;
import app.munch.model.Mention;
import app.munch.model.MentionStatus;
import app.munch.model.MentionType;
import app.munch.model.Profile;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.jpa.EntityPatch;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;
import dev.fuxing.transport.service.TransportContext;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

/**
 * Date: 28/9/19
 * Time: 10:48 pm
 *
 * @author Fuxing Loh
 */
@Singleton
public final class MeMentionService extends DataService {

    private final MentionController mentionController;

    @Inject
    MeMentionService(MentionController mentionController) {
        this.mentionController = mentionController;
    }

    @Override
    public void route() {
        PATH("/me/mentions", () -> {
            GET("", this::query);

            PATH("/:id", () -> {
                GET("", this::idGet);
                PATCH("", this::idPatch);
            });
        });
    }

    public TransportList query(TransportContext ctx) {
        final String accountId = ctx.get(ApiRequest.class).getAccountId();
        final int size = ctx.querySize(20, 40);
        final Set<MentionStatus> statuses = MentionStatus.fromQueryString(ctx.queryString("statuses", null));
        final Set<MentionType> types = MentionType.fromQueryString(ctx.queryString("types", null));
        TransportCursor cursor = ctx.queryCursor();

        return mentionController.queryByMe(accountId, size, statuses, types, cursor);
    }

    public Mention idGet(TransportContext ctx) {
        final String id = ctx.pathString("id");
        final String accountId = ctx.get(ApiRequest.class).getAccountId();

        return provider.reduce(true, entityManager -> {
            Mention mention = entityManager.find(Mention.class, id);
            if (mention == null) return null;

            Profile.authorize(entityManager, accountId, mention.getProfile());
            return mention;
        });
    }

    public Mention idPatch(TransportContext ctx) {
        final String id = ctx.pathString("id");
        final String accountId = ctx.get(ApiRequest.class).getAccountId();
        final JsonNode body = ctx.bodyAsJson();

        return provider.reduce(entityManager -> {
            Mention mention = entityManager.find(Mention.class, id);
            if (mention == null) return null;
            Profile.authorize(entityManager, accountId, mention.getProfile());

            return EntityPatch.with(entityManager, mention, body)
                    .patch("status", MentionStatus.class, Mention::setStatus)
                    .persist(mention1 -> {
                        mention1.setCreatedBy(mention1.getProfile());
                    });
        });
    }
}
