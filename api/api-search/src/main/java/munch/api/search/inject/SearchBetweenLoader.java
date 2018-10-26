package munch.api.search.inject;

import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 26/10/18
 * Time: 1:22 PM
 * Project: munch-core
 */
@Singleton
public final class SearchBetweenLoader implements SearchCardInjector.Loader {

    @Override
    public List<Position> load(Request request) {
        // Injected card above, wait for YZ
        return List.of();
    }
}
