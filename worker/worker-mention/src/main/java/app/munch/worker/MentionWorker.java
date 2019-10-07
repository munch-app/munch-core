package app.munch.worker;

import app.munch.model.WorkerTask;

import javax.inject.Singleton;

/**
 * @author Fuxing Loh
 * @since 7/10/19 at 5:00 pm
 */
@Singleton
public final class MentionWorker implements WorkerRunner {
    @Override
    public String groupUid() {
        return null;
    }

    @Override
    public void run(WorkerTask task) throws Exception {

    }

    public static void main(String[] args) {
        WorkerRunner.start(MentionWorker.class);
    }
}
