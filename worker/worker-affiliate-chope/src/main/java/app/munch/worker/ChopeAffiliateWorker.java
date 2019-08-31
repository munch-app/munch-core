package app.munch.worker;

import app.munch.database.DatabaseModule;
import app.munch.worker.data.WorkerReport;
import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.fuxing.jpa.TransactionProvider;

import javax.inject.Inject;

/**
 * Created by: Fuxing
 * Date: 30/8/19
 * Time: 1:01 PM
 * Project: munch-core
 */
public final class ChopeAffiliateWorker implements Worker {

    private final TransactionProvider provider;

    @Inject
    ChopeAffiliateWorker(TransactionProvider provider) {
        this.provider = provider;
    }

    @Override
    public String group() {
        return "chope.co";
    }

    @Override
    public String name() {
        return "Chope Affiliate Program";
    }

    @Override
    public String description() {
        return "Synchronization of chope.co affiliate links into munch eco system.";
    }

    @Override
    public void run(WorkerReport report) throws Exception {
        // TODO(fuxing): Sync to database

    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new WorkerModule(), new DatabaseModule());
        injector.getInstance(WorkerRunner.class).run(
                injector.getInstance(ChopeAffiliateWorker.class)
        );
    }
}
