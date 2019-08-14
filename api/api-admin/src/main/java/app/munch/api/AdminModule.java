package app.munch.api;

/**
 * Created by: Fuxing
 * Date: 2019-08-12
 * Time: 14:35
 */
public final class AdminModule extends ApiServiceModule {
    @Override
    protected void configure() {
        addService(ProfileAdminService.class);
        addService(ManagedPageAdminService.class);
        addService(PublicationAdminService.class);
        addService(TagAdminService.class);
    }
}
