package app.munch.api.v22v23;

import app.munch.model.Tag;
import app.munch.model.TagType;
import dev.fuxing.err.ForbiddenException;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.transport.service.TransportResult;
import dev.fuxing.transport.service.TransportService;
import munch.data.client.TagClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 19/8/19
 * Time: 4:42 am
 */
@Singleton
public final class MigrationService implements TransportService {

    private final TransactionProvider provider;
    private final TagClient tagClient;

    @Inject
    MigrationService(TransactionProvider provider, TagClient tagClient) {
        this.provider = provider;
        this.tagClient = tagClient;
    }

    @Override
    public void route() {
        PATH("/v22-v23/migrations", () -> {
            POST("/tags", this::tags);
        });
    }

    public TransportResult tags(TransportContext ctx) {
        provider.with(entityManager -> {
            List list = entityManager.createQuery("FROM Tag")
                    .setMaxResults(1)
                    .getResultList();

            if (!list.isEmpty()) {
                throw new ForbiddenException("Only can run it once.");
            }

            tagClient.iterator().forEachRemaining(deprecatedTag -> {
                Tag tag = new Tag();
                tag.setType(parse(deprecatedTag.getType().name()));
                tag.setName(deprecatedTag.getName());
                entityManager.persist(tag);
            });
        });
        return TransportResult.ok();
    }

    private TagType parse(String type) {
        switch (type) {
            case "Food":
                return TagType.FOOD;
            case "Cuisine":
                return TagType.CUISINE;
            case "Amenities":
            case "Establishment":
            case "Timing":
            case "Requirement":
            default:
                return TagType.AMENITIES;
        }
    }
}
