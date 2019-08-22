package app.munch.api;

/**
 * Created by: Fuxing
 * Date: 22/8/19
 * Time: 9:34 pm
 */
public final class PageModule extends ApiServiceModule {
    @Override
    protected void configure() {
        addService(PageService.class);
    }
}
