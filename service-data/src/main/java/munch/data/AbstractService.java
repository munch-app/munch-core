package munch.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.munch.hibernate.utils.TransactionProvider;
import munch.data.database.AbstractEntity;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 16/8/2017
 * Time: 9:23 PM
 * Project: munch-core
 */
public abstract class AbstractService<D, T extends AbstractEntity<D>> implements JsonService {
    protected final String serviceName;

    @Inject
    protected TransactionProvider provider;
    protected final Class<T> entityClass;
    protected final String entityName;

    protected final Class<D> dataClass;

    protected AbstractService(String serviceName, Class<T> entityClass, Class<D> dataClass) {
        this.serviceName = serviceName;
        this.entityName = entityClass.getSimpleName();
        this.entityClass = entityClass;
        this.dataClass = dataClass;
    }

    @Override
    public void route() {
        PATH("/" + serviceName, () -> {
            POST("/get", this::batchGet);

            GET("/:id", this::get);
            PUT("/:id", this::put);
            DELETE("/:id", this::delete);
        });
    }

    protected List<D> batchGet(JsonCall call) {
        List<String> keys = call.bodyAsList(String.class);

        if (keys.isEmpty()) return Collections.emptyList();

        Map<String, D> placeMap = provider.reduce(em -> em.createQuery(
                "FROM " + entityName + " WHERE id IN (:keys)", entityClass)
                .setParameter("keys", keys)
                .getResultList())
                .stream()
                .collect(Collectors.toMap(AbstractEntity::getId, AbstractEntity::getData));
        return keys.stream().map(placeMap::get).collect(Collectors.toList());
    }

    protected D get(JsonCall call) {
        String id = call.pathString("id");

        return provider.optional(em -> em.find(entityClass, id))
                .map(AbstractEntity::getData)
                .orElse(null);
    }

    protected JsonNode put(JsonCall call) {
        D data = call.bodyAsObject(dataClass);
//        call.bodyAsObject()

//        provider.with(em -> persist(em, place));

        return Meta200;
    }

    protected JsonNode delete(JsonCall call) {

        return Meta200;
    }
}
