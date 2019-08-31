package app.munch.worker.data;

import app.munch.worker.Worker;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import dev.fuxing.err.ExceptionParser;
import dev.fuxing.err.TransportException;
import dev.fuxing.err.UnknownException;
import dev.fuxing.err.ValidationException;
import dev.fuxing.transport.TransportError;
import dev.fuxing.utils.JsonUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 30/8/19
 * Time: 12:24 PM
 * Project: munch-core
 */
@Singleton
public final class WorkerGroupManager {

    private final Table table;

    @Inject
    WorkerGroupManager(DynamoDB dynamoDB) {
        this.table = dynamoDB.getTable("munch-core.WorkerGroup");
    }

    public WorkerGroup start(Worker worker) {
        WorkerGroup group = load(worker);

        WorkerReport report = new WorkerReport();
        report.setStatus(WorkerReportStatus.STARTED);
        report.setStartedAt(new Date());
        group.getReports().add(0, report);

        save(group);
        return group;
    }

    public void complete(WorkerGroup group) {
        WorkerReport report = group.getReports().get(0);

        report.setStatus(WorkerReportStatus.COMPLETED);
        report.setCompletedAt(new Date());

        save(group);
    }

    public void error(WorkerGroup group, Exception exception) {
        WorkerReport report = group.getReports().get(0);

        report.setStatus(WorkerReportStatus.ERROR);
        report.setCompletedAt(new Date());
        report.setDetails(JsonUtils.createObjectNode(nodes -> {
            nodes.set("exception", JsonUtils.valueToTree(serializeException(exception)));
        }));

        save(group);
    }

    private TransportError serializeException(Exception exception) {
        try {
            ExceptionParser.parse(exception);
        } catch (TransportException e) {
            return e.toError();
        }

        return new UnknownException(exception).toError();
    }

    private WorkerGroup load(Worker worker) {
        Item item = table.getItem("group", worker.group());
        WorkerGroup group;
        if (item != null) {
            group = JsonUtils.toObject(item.toJSON(), WorkerGroup.class);
        } else {
            group = new WorkerGroup();
            group.setGroup(worker.group());
            group.setReports(new ArrayList<>());
        }

        group.setName(worker.name());
        group.setDescription(worker.description());
        return group;
    }

    private void save(WorkerGroup group) {
        ValidationException.validate(group);

        group.setReports(group.getReports().stream()
                .limit(4)
                .collect(Collectors.toList()));

        String json = JsonUtils.toString(group);
        Item item = Item.fromJSON(json);
        table.putItem(item);
    }
}
