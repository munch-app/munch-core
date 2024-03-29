package munch.api.search.assumption.data;

/**
 * Created by: Fuxing
 * Date: 17/3/2018
 * Time: 7:09 PM
 * Project: munch-data
 */
public final class TextAssumptionToken extends AssumptionToken {
    public TextAssumptionToken(String text) {
        super(text);
    }

    @Override
    public String getType() {
        return "text";
    }
}
