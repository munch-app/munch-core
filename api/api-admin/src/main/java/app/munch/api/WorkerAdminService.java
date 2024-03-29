package app.munch.api;

import app.munch.model.WorkerGroup;
import dev.fuxing.jpa.EntityStream;
import dev.fuxing.transport.TransportList;
import dev.fuxing.transport.service.TransportContext;

import javax.inject.Singleton;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 6/9/19
 * Time: 10:39 am
 */
@Singleton
public final class WorkerAdminService extends AdminService {

    @Override
    public void route() {
        PATH("/admin/workers/groups", () -> {
            GET("", this::list);

            PATH("/:uid", () -> {
                GET("", this::get);
            });
        });
    }

    public TransportList list(TransportContext ctx) {
        int size = ctx.querySize(10, 30);
        return provider.reduce(true, entityManager -> {
            return EntityStream.of(() -> {
                return entityManager.createQuery("FROM WorkerGroup " +
                        "ORDER BY uid DESC", WorkerGroup.class)
                        .setMaxResults(size)
                        .getResultList();
            }).peek(o -> {
                o.setReports(o.getReports().stream().limit(3)
                        .collect(Collectors.toList()));
            }).asTransportList();
        });
    }

    public WorkerGroup get(TransportContext ctx) {
        String uid = ctx.pathString("uid");

        return provider.reduce(true, entityManager -> {
            WorkerGroup workerGroup = entityManager.find(WorkerGroup.class, uid);
            workerGroup.setReports(workerGroup.getReports().stream().limit(3)
                    .collect(Collectors.toList()));
            return workerGroup;
        });
    }
}
