package app.munch.api;

import app.munch.image.ImageModule;

/**
 * Created by: Fuxing
 * Date: 2019-08-12
 * Time: 06:07
 */
public final class DataModule extends ApiServiceModule {
    @Override
    protected void configure() {
        install(new ImageModule());

        addHealth(DataHealthCheck.class);

        addService(MeService.class);
        addService(ProfileService.class);

        addService(ImageService.class);

        addService(ArticleService.class);
        addService(PublicationService.class);

        addService(TagService.class);
        addService(PlaceService.class);

        addService(UrlRedirectionService.class);
    }
}
