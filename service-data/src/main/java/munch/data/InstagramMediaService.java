package munch.data;

import com.google.inject.Singleton;
import munch.data.database.InstagramMediaEntity;
import munch.restful.server.JsonCall;

import javax.inject.Inject;
import java.util.List;
import java.util.function.Function;

/**
 * Created By: Fuxing Loh
 * Date: 17/4/2017
 * Time: 4:13 PM
 * Project: munch-core
 */
@Singleton
public final class InstagramMediaService extends AbstractService<InstagramMediaEntity> {

    @Inject
    public InstagramMediaService() {
        super("/instagram/medias", InstagramMediaEntity.class);
    }

    @Override
    public void route() {
        super.route();
        GET("/places/:placeId/instagram/medias/list", this::list);
    }

    @Override
    protected Function<InstagramMediaEntity, String> getKeyMapper() {
        return InstagramMediaEntity::getMediaId;
    }

    @Override
    protected List<InstagramMediaEntity> getList(List<String> keys) {
        return provider.reduce(em -> em.createQuery(
                "FROM InstagramMediaEntity WHERE mediaId IN (:keys)", InstagramMediaEntity.class)
                .setParameter("keys", keys)
                .getResultList());
    }

    private List<InstagramMediaEntity> list(JsonCall call) {
        String placeId = call.pathString("placeId");
        int from = call.queryInt("from");
        int size = call.queryInt("size");
        return provider.reduce(em -> em.createQuery("FROM InstagramMediaEntity WHERE " +
                "placeId = :placeId ORDER BY createdDate desc", InstagramMediaEntity.class)
                .setParameter("placeId", placeId)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList());
    }
}
