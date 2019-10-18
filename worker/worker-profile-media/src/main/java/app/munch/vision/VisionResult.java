package app.munch.vision;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Fuxing Loh
 * @since 2019-10-18 at 12:44 am
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class VisionResult {

    private VisionResultType name;

    private double confident;

    public VisionResultType getName() {
        return name;
    }

    public void setName(VisionResultType name) {
        this.name = name;
    }

    public double getConfident() {
        return confident;
    }

    public void setConfident(double confident) {
        this.confident = confident;
    }

    @Override
    public String toString() {
        return "VisionResult{" +
                "name=" + name +
                ", confident=" + confident +
                '}';
    }
}
