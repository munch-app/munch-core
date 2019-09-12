package app.munch.model;

import com.fasterxml.jackson.annotation.*;
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
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(Status.OpenStatus.class),
        @JsonSubTypes.Type(Status.DormantStatus.class),
        @JsonSubTypes.Type(Status.HiddenStatus.class),
        @JsonSubTypes.Type(Status.MovedStatus.class),
        @JsonSubTypes.Type(Status.DeletedStatus.class),
        @JsonSubTypes.Type(Status.PermanentlyClosedStatus.class),
})
public interface Status {

    @ValidEnum
    @JsonIgnore
    StatusType getType();

    static boolean equals(Status left, Status right) {
        if (left == null && right == null) return true;
        if (left == null || right == null) return false;

        return left.equals(right);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonTypeName("OPEN")
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
    @JsonTypeName("DORMANT")
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
    @JsonTypeName("HIDDEN")
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
    @JsonTypeName("DELETED")
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
    @JsonTypeName("MOVED")
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
    @JsonTypeName("PERMANENTLY_CLOSED")
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
