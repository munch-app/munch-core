package munch.instagram;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.hibernate.utils.TransactionProvider;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;

import java.util.Date;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 7:00 PM
 * Project: munch-core
 */
@Singleton
public class MediaService implements JsonService {

    private final TransactionProvider provider;

    @Inject
    public MediaService(TransactionProvider provider) {
        this.provider = provider;
    }

    @Override
    public void route() {
        PATH("/places/:placeId/instagram/medias", () -> {
            GET("/list", this::list);
            GET("/:mediaId", this::get);

            // Management
            PUT("/:mediaId", this::put);
            DELETE("/before/:timestamp", this::deleteBefore);
            DELETE("/:mediaId", this::delete);
        });
    }

    private List<InstagramMedia> list(JsonCall call) {
        String placeId = call.pathString("placeId");
        int from = call.queryInt("from");
        int size = call.queryInt("size");
        return provider.reduce(em -> em.createQuery("FROM InstagramMedia WHERE " +
                "placeId = :placeId ORDER BY createdDate desc", InstagramMedia.class)
                .setParameter("placeId", placeId)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList());
    }

    private InstagramMedia get(JsonCall call) {
        String mediaId = call.pathString("mediaId");
        return provider.reduce(em -> em.find(InstagramMedia.class, mediaId));
    }

    private InstagramMedia put(JsonCall call) {
        InstagramMedia media = call.bodyAsObject(InstagramMedia.class);
        provider.with(em -> {
            if (em.find(InstagramMedia.class, media.getMediaId()) == null) {
                em.persist(media);
            } else {
                em.merge(media);
            }
        });
        return media;
    }

    private JsonNode delete(JsonCall call) {
        String mediaId = call.pathString("mediaId");
        provider.with(em -> em.createQuery("DELETE FROM InstagramMedia WHERE mediaId = :mediaId")
                .setParameter("mediaId", mediaId)
                .executeUpdate());
        return Meta200;
    }

    private JsonNode deleteBefore(JsonCall call) {
        String placeId = call.pathString("placeId");
        Date before = new Date(call.pathLong("timestamp"));
        provider.with(em -> em.createQuery("DELETE FROM InstagramMedia WHERE placeId = :id AND updatedDate < :before")
                .setParameter("id", placeId)
                .setParameter("before", before)
                .executeUpdate());
        return Meta200;
    }
}
