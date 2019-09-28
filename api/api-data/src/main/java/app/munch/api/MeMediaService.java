package app.munch.api;

import javax.inject.Singleton;

/**
 * Date: 28/9/19
 * Time: 10:48 pm
 *
 * @author Fuxing Loh
 */
@Singleton
public final class MeMediaService extends DataService {

    @Override
    public void route() {
        PATH("/me/medias", () -> {
            PATH("/:uid", () -> {
                // No implementation required yet as of 0.25.0
            });
        });
    }
}
