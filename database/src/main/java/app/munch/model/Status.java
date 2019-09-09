package app.munch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.fuxing.validator.ValidEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

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
        @JsonSubTypes.Type(value = Status.DormantStatus.class, name = "DORMANT"),
        @JsonSubTypes.Type(value = Status.HiddenStatus.class, name = "HIDDEN"),
        @JsonSubTypes.Type(value = Status.MovedStatus.class, name = "MOVED"),
        @JsonSubTypes.Type(value = Status.DeletedStatus.class, name = "DELETED"),
        @JsonSubTypes.Type(value = Status.PermanentlyClosedStatus.class, name = "PERMANENTLY_CLOSED"),
})
public interface Status {

    @ValidEnum
    StatusType getType();

    static boolean equals(Status left, Status right) {
        if (left == null && right == null) return true;
        if (left == null || right == null) return false;

        return left.equals(right);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    final class OpenStatus implements Status {
        @Override
        public StatusType getType() {
            return StatusType.OPEN;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            return true;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    final class DormantStatus implements Status {

        @NotNull
        private Date at;

        @Override
        public @ValidEnum StatusType getType() {
            return StatusType.DORMANT;
        }

        public Date getAt() {
            return at;
        }

        public void setAt(Date at) {
            this.at = at;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DormantStatus s = (DormantStatus) o;
            return Objects.equals(at, s.at);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    final class HiddenStatus implements Status {

        @Override
        public @ValidEnum StatusType getType() {
            return StatusType.HIDDEN;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            return true;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DeletedStatus s = (DeletedStatus) o;
            return Objects.equals(reason, s.reason) &&
                    Objects.equals(at, s.at);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    final class MovedStatus implements Status {

        @NotNull
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MovedStatus s = (MovedStatus) o;
            return Objects.equals(id, s.id) &&
                    Objects.equals(at, s.at);
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PermanentlyClosedStatus s = (PermanentlyClosedStatus) o;
            return Objects.equals(at, s.at);
        }
    }
}
