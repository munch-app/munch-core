package munch.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.munch.hibernate.utils.TransactionProvider;
import munch.data.database.CycleEntity;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 16/8/2017
 * Time: 9:23 PM
 * Project: munch-core
 */
public abstract class AbstractService<T extends CycleEntity> implements JsonService {
    protected final String serviceName;

    @Inject
    protected TransactionProvider provider;
    protected final Class<T> entityClass;
    protected final String entityName;

    protected AbstractService(String serviceName, Class<T> entityClass) {
        this.serviceName = serviceName;
        this.entityName = entityClass.getSimpleName();
        this.entityClass = entityClass;
    }

    @Override
    public void route() {
        PATH(serviceName, () -> {
            POST("/get", this::batchGet);

            GET("/:id", this::get);
            PUT("/:cycleNo/:id", this::put);

            DELETE("/:cycleNo/before", this::deleteBefore);
            DELETE("/:cycleNo/:id", this::delete);
        });
    }

    protected abstract Function<T, String> getKeyMapper();

    protected abstract List<T> getList(List<String> keys);

    protected List<T> batchGet(JsonCall call) {
        List<String> keys = call.bodyAsList(String.class);

        if (keys.isEmpty()) return Collections.emptyList();

        Map<String, T> placeMap = getList(keys).stream()
                .collect(Collectors.toMap(getKeyMapper(), Function.identity()));
        return keys.stream().map(placeMap::get).collect(Collectors.toList());
    }

    protected T get(JsonCall call) {
        String id = call.pathString("id");

        return provider.optional(em -> em.find(entityClass, id))
                .orElse(null);
    }

    protected JsonNode put(JsonCall call) {
        // String id = call.pathString("id");
        long cycleNo = call.pathLong("cycleNo");


        T data = call.bodyAsObject(entityClass);
        data.setCycleNo(cycleNo);

        provider.with(em -> {
            em.merge(entityClass);

            // TODO if Merge works
//            if (em.find(entityClass, id) == null) {
//                em.persist(entityClass);
//            } else {
//                em.merge(entityClass);
//            }
        });
        return Meta200;
    }

    private JsonNode deleteBefore(JsonCall call) {
        long cycleNo = call.pathLong("cycleNo");

        provider.with(em -> em.createQuery(
                "DELETE FROM " + entityName + " WHERE cycleNo < :cycleNo", entityClass)
                .setParameter("cycleNo", cycleNo)
                .executeUpdate());
        return Meta200;
    }

    protected JsonNode delete(JsonCall call) {
        String id = call.pathString("id");
        provider.with(em -> em.remove(em.find(entityClass, id)));
        return Meta200;
    }
}
