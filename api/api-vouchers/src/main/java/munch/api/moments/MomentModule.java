package munch.api.moments;

import munch.api.ApiServiceModule;

/**
 * Created by: Fuxing
 * Date: 2019-03-11
 * Time: 00:18
 * Project: munch-core
 */
public final class MomentModule extends ApiServiceModule {
    @Override
    protected void configure() {
        addService(VoucherService.class);
    }
}
