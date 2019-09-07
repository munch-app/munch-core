package app.munch.worker;

import app.munch.model.WorkerGroup;
import app.munch.model.WorkerTask;
import app.munch.model.WorkerTaskStatus;
import dev.fuxing.err.ExceptionParser;
import dev.fuxing.err.TransportException;
import dev.fuxing.err.UnknownException;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.TransportError;
import dev.fuxing.utils.JsonUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 30/8/19
 * Time: 12:24 PM
 * Project: munch-core
 */
@Singleton
public final class WorkerCoordinator {

    private final TransactionProvider provider;

    @Inject
    WorkerCoordinator(TransactionProvider provider) {
        this.provider = provider;
    }

    public WorkerTask start(String groupUid) {
        return provider.reduce(entityManager -> {
            WorkerGroup group = entityManager.find(WorkerGroup.class, groupUid);

            WorkerTask task = new WorkerTask();
            task.setGroup(group);
            task.setStatus(WorkerTaskStatus.STARTED);
            entityManager.persist(task);
            return task;
        });
    }

    public WorkerTask complete(WorkerTask task) {
        return provider.reduce(entityManager -> {
            WorkerTask entity = entityManager.find(WorkerTask.class, task.getUid());
            entity.setStatus(WorkerTaskStatus.COMPLETED);
            entityManager.persist(entity);
            return entity;
        });
    }

    public WorkerTask error(WorkerTask task, Exception exception) {
        return provider.reduce(entityManager -> {
            WorkerTask entity = entityManager.find(WorkerTask.class, task.getUid());

            entity.setStatus(WorkerTaskStatus.ERROR);
            entity.setDetails(JsonUtils.createObjectNode(nodes -> {
                nodes.set("exception", JsonUtils.valueToTree(serializeException(exception)));
            }));
            entityManager.persist(entity);
            return entity;
        });
    }

    private static TransportError serializeException(Exception exception) {
        try {
            ExceptionParser.parse(exception);
        } catch (TransportException e) {
            return e.toError();
        }

        return new UnknownException(exception).toError();
    }
}
