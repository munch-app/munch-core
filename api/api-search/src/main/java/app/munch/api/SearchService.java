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
public final class SearchService implements TransportService {
    @Override
    public void route() {
        PATH("/search", () -> {
            POST("", this::search);
            POST("/suggest", this::suggest);
        });
    }

    public TransportResult search(TransportContext ctx) {
        return TransportResult.ok();
    }

    public TransportResult suggest(TransportContext ctx) {
        return TransportResult.ok();
    }
}
