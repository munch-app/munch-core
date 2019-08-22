package app.munch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.fuxing.validator.ValidEnum;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 2019-08-08
 * Time: 20:53
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Hour {

    @ValidEnum
    @Enumerated(EnumType.STRING)
    private Day day;

    @NotNull
    @Min(0)
    @Max(1440)
    private Integer open;

    @NotNull
    @Min(0)
    @Max(1440)
    private Integer close;

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public Integer getOpen() {
        return open;
    }

    public void setOpen(Integer open) {
        this.open = open;
    }

    public Integer getClose() {
        return close;
    }

    public void setClose(Integer close) {
        this.close = close;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hour hour = (Hour) o;
        return day == hour.day &&
                open.equals(hour.open) &&
                close.equals(hour.close);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, open, close);
    }
}