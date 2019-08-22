package app.munch.api;

import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.transport.service.TransportService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 22/8/19
 * Time: 9:34 pm
 */
@Singleton
public final class PageService implements TransportService {

    private final PageResolver pageResolver;

    @Inject
    PageService(PageResolver pageResolver) {
        this.pageResolver = pageResolver;
    }

    @Override
    public void route() {
        PATH("/pages", () -> {
            GET("/:name", this::name);
        });
    }

    public List<Map> name(TransportContext ctx) {
        String name = ctx.pathString("name");
        return pageResolver.resolve(name);
    }
}
