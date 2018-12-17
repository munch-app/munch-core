package munch.api.search.cards;

import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 17/12/18
 * Time: 4:03 AM
 * Project: munch-core
 */
public final class SearchHomeDTJECard implements SearchCard {

    private final List<String> lunch;
    private final List<String> dinner;

    public SearchHomeDTJECard(List<String> lunch, List<String> dinner) {
        this.lunch = lunch;
        this.dinner = dinner;
    }

    public List<String> getLunch() {
        return lunch;
    }

    public List<String> getDinner() {
        return dinner;
    }

    @Override
    public @Pattern(regexp = "[a-z-0]{1,64}_[0-9]{4}-[0-9]{2}-[0-9]{2}", flags = Pattern.Flag.CASE_INSENSITIVE) String getCardId() {
        return "HomeDTJE_2018-12-17";
    }
}
