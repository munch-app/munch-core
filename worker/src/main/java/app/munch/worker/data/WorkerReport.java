package app.munch.worker.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.validator.ValidEnum;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 30/8/19
 * Time: 12:19 PM
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorkerReport {

    @ValidEnum
    // @Enumerated(EnumType.STRING)
    //     @Column(length = 100, updatable = true, nullable = false, unique = false)
    private WorkerReportStatus status;

    @NotNull
    private Date startedAt;

    private Date completedAt;

    private JsonNode details;

    public WorkerReportStatus getStatus() {
        return status;
    }

    public void setStatus(WorkerReportStatus status) {
        this.status = status;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    public JsonNode getDetails() {
        return details;
    }

    public void setDetails(JsonNode details) {
        this.details = details;
    }
}
