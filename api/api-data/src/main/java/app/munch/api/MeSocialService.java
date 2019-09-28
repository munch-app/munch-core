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
public final class MeSocialService extends DataService {

    @Override
    public void route() {
        PATH("/me/socials", () -> {
            GET("", this::list);
            POST("/authenticate", this::authenticate);

            PATH("/:uid", () -> {
                GET("", this::uidGet);
                PATCH("", this::uidPatch);
            });
        });
    }

    // TODO(fuxing): implementation

    public TransportResult authenticate(TransportContext ctx) {

        return TransportResult.ok();
    }

    public TransportResult list(TransportContext ctx) {

        return TransportResult.ok();
    }

    public TransportResult uidGet(TransportContext ctx) {

        return TransportResult.ok();
    }

    public TransportResult uidPatch(TransportContext ctx) {

        return TransportResult.ok();
    }
}
