package munch.api.moments;

import com.fasterxml.jackson.databind.JsonNode;
import munch.api.ApiRequest;
import munch.api.ApiService;
import munch.restful.core.exception.ForbiddenException;
import munch.restful.server.JsonCall;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Created by: Fuxing
 * Date: 2019-03-11
 * Time: 00:18
 * Project: munch-core
 */
@Singleton
public final class VoucherService extends ApiService {

    private final VoucherDatabase database;

    @Inject
    public VoucherService(VoucherDatabase database) {
        this.database = database;
    }

    @Override
    public void route() {
        PATH("/vouchers/:voucherId", () -> {
            GET("", this::get);
            POST("/claim", this::claim);
        });
    }

    public Voucher get(JsonCall call, ApiRequest request) {
        final String userId = request.getUserId();
        final String voucherId = call.pathString("voucherId");
        return database.get(voucherId, userId, getDateTime(request));
    }

    /**
     * @throws ForbiddenException if claim cannot be fulfilled
     */
    public Voucher claim(JsonCall call, ApiRequest request) throws ForbiddenException {
        final String userId = request.getUserId();
        final String voucherId = call.pathString("voucherId");

        JsonNode body = call.bodyAsJson();
        return database.claim(voucherId, userId, body.path("passcode").asText(), getDateTime(request));
    }

    public LocalDateTime getDateTime(ApiRequest request) {
        LocalDateTime dateTime = request.getLocalDateTime();
        if (dateTime != null) return dateTime;

        return LocalDateTime.now(ZoneId.of("Asia/Singapore"));
    }

    // TODO: Send ZoneId in API Request?
}
