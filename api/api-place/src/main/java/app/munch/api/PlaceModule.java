package app.munch.api;

/**
 * Created by: Fuxing
 * Date: 9/9/19
 * Time: 2:41 pm
 */
public final class PlaceModule extends ApiServiceModule {
    @Override
    protected void configure() {
        addService(PlaceService.class);
    }
}
