package app.munch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.fuxing.validator.ValidEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 2019-08-06
 * Time: 18:54
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Status.OpenStatus.class, name = "OPEN"),
        @JsonSubTypes.Type(value = Status.MovedStatus.class, name = "MOVED"),
        @JsonSubTypes.Type(value = Status.DeletedStatus.class, name = "DELETED"),
        @JsonSubTypes.Type(value = Status.PermanentlyClosedStatus.class, name = "PERMANENTLY_CLOSED"),
})
public interface Status {

    @ValidEnum
    StatusType getType();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    final class OpenStatus implements Status {
        @Override
        public StatusType getType() {
            return StatusType.OPEN;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    final class DeletedStatus implements Status {

        @Length(max = 255)
        private String reason;

        @NotNull
        private Date at;

        @Override
        public StatusType getType() {
            return StatusType.DELETED;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public Date getAt() {
            return at;
        }

        public void setAt(Date at) {
            this.at = at;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    final class MovedStatus implements Status {

        @NotNull
        @Pattern(regexp = "^[0-9a-hjkmnp-tv-z]{12}0$")
        private String id;

        @NotNull
        private Date at;

        @Override
        public StatusType getType() {
            return StatusType.MOVED;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Date getAt() {
            return at;
        }

        public void setAt(Date at) {
            this.at = at;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    final class PermanentlyClosedStatus implements Status {

        @NotNull
        private Date at;

        @Override
        public StatusType getType() {
            return StatusType.PERMANENTLY_CLOSED;
        }

        public Date getAt() {
            return at;
        }

        public void setAt(Date at) {
            this.at = at;
        }
    }
}
