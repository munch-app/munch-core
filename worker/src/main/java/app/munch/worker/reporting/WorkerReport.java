package app.munch.worker.reporting;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.utils.KeyUtils;
import dev.fuxing.validator.ValidEnum;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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

    @NotNull
    @Pattern(regexp = "^[a-z0-9.]{1,100}$")
    private String group;

    @NotNull
    @Pattern(regexp = KeyUtils.ULID_REGEX)
    private String uid;

    @ValidEnum
    private WorkerReportStatus status;

    @NotNull
    private Date startedAt;

    private Date completedAt;

    private JsonNode details;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public WorkerReportStatus getStatus() {
        return status;
    }

    public void setStatus(WorkerReportStatus status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
