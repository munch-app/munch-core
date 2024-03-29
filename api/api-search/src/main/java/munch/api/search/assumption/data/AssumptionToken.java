package munch.api.search.assumption.data;

import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 17/3/2018
 * Time: 7:09 PM
 * Project: munch-data
 */
public abstract class AssumptionToken {
    private final String text;

    public AssumptionToken(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public abstract String getType();

    @Override
    public String toString() {
        return getType() + "[" + text + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssumptionToken that = (AssumptionToken) o;
        return Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
