package app.munch.api;

/**
 * Created by: Fuxing
 * Date: 2019-08-12
 * Time: 14:35
 */
public final class AdminModule extends ApiServiceModule {
    @Override
    protected void configure() {
        addService(TagAdminService.class);
        addService(ProfileAdminService.class);

        addService(PublicationAdminService.class);
        addService(ArticleAdminSystem.class);

        addService(WorkerAdminService.class);
        addService(ArticleAdminSystem.class);

        addService(AffiliateAdminService.class);
    }
}
