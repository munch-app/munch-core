package app.munch.api;

/**
 * Created by: Fuxing
 * Date: 14/8/19
 * Time: 12:22 pm
 */
public final class AccountModule extends ApiServiceModule {
    @Override
    protected void configure() {
        addService(AccountService.class);
    }
}
