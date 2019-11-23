package app.munch.api;

/**
 * @author Fuxing Loh
 * @since 2019-11-22 at 22:00
 */
public final class SearchModule extends ApiServiceModule {
    @Override
    protected void configure() {
        addService(SearchService.class);
        addService(FilterService.class);
    }
}
