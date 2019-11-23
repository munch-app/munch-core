package app.munch.api;

import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.transport.service.TransportResult;
import dev.fuxing.transport.service.TransportService;

import javax.inject.Singleton;

/**
 * @author Fuxing Loh
 * @since 2019-11-22 at 22:00
 */
@Singleton
public final class FilterService implements TransportService {
    @Override
    public void route() {
        PATH("/filter", () -> {
            POST("", this::filter);
        });
    }

    public TransportResult filter(TransportContext ctx) {

        return TransportResult.ok();
    }
}
