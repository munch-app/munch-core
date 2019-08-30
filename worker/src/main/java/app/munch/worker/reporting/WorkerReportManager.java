package app.munch.worker.reporting;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import dev.fuxing.err.ExceptionParser;
import dev.fuxing.err.TransportException;
import dev.fuxing.err.UnknownException;
import dev.fuxing.err.ValidationException;
import dev.fuxing.transport.TransportError;
import dev.fuxing.utils.JsonUtils;
import dev.fuxing.utils.KeyUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 30/8/19
 * Time: 12:24 PM
 * Project: munch-core
 */
@Singleton
public final class WorkerReportManager {

    private final Table table;

    @Inject
    WorkerReportManager(DynamoDB dynamoDB) {
        this.table = dynamoDB.getTable("munch-core.WorkerReport");
    }

    public WorkerReport start(String group) {
        WorkerReport report = new WorkerReport();
        report.setGroup(group);
        report.setUid(KeyUtils.nextULID());

        report.setStatus(WorkerReportStatus.STARTED);
        report.setStartedAt(new Date());

        save(report);
        return report;
    }

    public void complete(WorkerReport report) {
        report.setStatus(WorkerReportStatus.COMPLETED);
        report.setCompletedAt(new Date());
        save(report);
    }

    public void error(WorkerReport report, Exception exception) {
        report.setStatus(WorkerReportStatus.ERROR);
        report.setCompletedAt(new Date());
        report.setDetails(JsonUtils.createObjectNode(nodes -> {
            nodes.set("exception", JsonUtils.valueToTree(serializeException(exception)));
        }));

        save(report);
    }

    private TransportError serializeException(Exception exception) {
        try {
            ExceptionParser.parse(exception);
        } catch (TransportException e) {
            return e.toError();
        }

        return new UnknownException(exception).toError();
    }

    private void save(WorkerReport report) {
        ValidationException.validate(report);

        String json = JsonUtils.toString(report);
        Item item = Item.fromJSON(json);
        table.putItem(item);
    }
}
