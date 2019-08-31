package app.munch.worker.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 31/8/19
 * Time: 5:46 pm
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorkerGroup {

    @NotNull
    @Pattern(regexp = "^[a-z0-9.]{1,100}$")
    private String group;

    @NotBlank
    @Length(max = 100)
    private String name;

    @NotBlank
    @Length(max = 500)
    private String description;

    /**
     * Keep a maximum of 4 reports per worker group
     */
    @Size(max = 4)
    @Valid
    private List<WorkerReport> reports;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<WorkerReport> getReports() {
        return reports;
    }

    public void setReports(List<WorkerReport> reports) {
        this.reports = reports;
    }
}
