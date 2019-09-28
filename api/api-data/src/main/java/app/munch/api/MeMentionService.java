package app.munch.api;

import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.transport.service.TransportResult;

import javax.inject.Singleton;

/**
 * Date: 28/9/19
 * Time: 10:48 pm
 *
 * @author Fuxing Loh
 */
@Singleton
public final class MeMentionService extends DataService {
    @Override
    public void route() {
        PATH("/me/mentions", () -> {
            GET("", this::query);

            PATH("/:id", () -> {
                GET("", this::idGet);
                PATCH("", this::idPatch);
            });
        });
    }

    // TODO(fuxing): implementation

    public TransportResult query(TransportContext ctx) {

        return TransportResult.ok();
    }

    public TransportResult idGet(TransportContext ctx) {
        return TransportResult.ok();
    }

    public TransportResult idPatch(TransportContext ctx) {

        return TransportResult.ok();
    }
}
