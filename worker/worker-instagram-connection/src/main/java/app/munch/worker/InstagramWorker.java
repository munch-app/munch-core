package app.munch.worker;

import app.munch.database.DatabaseModule;
import app.munch.model.*;
import com.instagram.err.InstagramAccessException;
import com.instagram.err.InstagramException;
import dev.fuxing.jpa.HibernateUtils;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.utils.SleepUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.List;

/**
 * Date: 2/10/19
 * Time: 9:55 am
 *
 * @author Fuxing Loh
 */
@Singleton
public final class InstagramWorker implements WorkerRunner {
    private static final Logger logger = LoggerFactory.getLogger(InstagramWorker.class);

    private final TransactionProvider provider;
    private final InstagramDataRiver dataRiver;

    @Inject
    InstagramWorker(TransactionProvider provider, InstagramDataRiver dataRiver) {
        this.provider = provider;
        this.dataRiver = dataRiver;
    }

    @Override
    public String groupUid() {
        return "01dp6jafyrdfvhbz07fsf6ndb3";
    }

    @Override
    public void run(WorkerTask task) {
        do {
            InstagramAccountConnectionTask connectionTask = acquire();
            if (connectionTask != null) {
                connect(connectionTask);
                continue;
            }

            SleepUtils.sleep(Duration.ofMinutes(1));
        } while (true);
    }

    /**
     * @return Acquired ConnectionTask
     */
    @Nullable
    private InstagramAccountConnectionTask acquire() {
        InstagramAccountConnectionTask acquired = provider.reduce(entityManager -> {
            List<InstagramAccountConnection> list = entityManager.createQuery("FROM InstagramAccountConnection " +
                    "WHERE status = :status " +
                    "ORDER BY connectedAt DESC", InstagramAccountConnection.class)
                    .setParameter("status", InstagramAccountConnectionStatus.CONNECTED)
                    .setMaxResults(1)
                    .getResultList();

            if (list.isEmpty()) return null;
            InstagramAccountConnection connection = list.get(0);

            InstagramAccountConnectionTask task = new InstagramAccountConnectionTask();
            task.setConnection(connection);
            entityManager.persist(task);

            connection.setConnectedAt(new Timestamp(System.currentTimeMillis()));
            entityManager.persist(task);
            return task;
        });

        // Acquired task, wait 10 seconds to reconfirm no one else did.
        SleepUtils.sleep(Duration.ofSeconds(10));

        return provider.reduce(true, entityManager -> {
            List<InstagramAccountConnectionTask> tasks = entityManager.createQuery("FROM InstagramAccountConnectionTask " +
                    "ORDER BY createdAt DESC ", InstagramAccountConnectionTask.class)
                    .setMaxResults(1)
                    .getResultList();

            InstagramAccountConnectionTask latest = tasks.get(0);
            if (latest.getUid().equals(acquired.getUid())) {
                HibernateUtils.initialize(latest.getConnection());
                HibernateUtils.initialize(latest.getConnection().getSocial());
                HibernateUtils.initialize(latest.getConnection().getSocial().getProfile());
                return latest;
            }

            // Someone other worker took latest spot.
            return null;
        });
    }

    private void connect(InstagramAccountConnectionTask connectionTask) {
        try {
            dataRiver.open(connectionTask);
        } catch (InstagramAccessException e) {
            provider.with(entityManager -> {
                InstagramAccountConnection connection = entityManager.find(InstagramAccountConnection.class, connectionTask.getConnection().getUid());
                connection.setStatus(InstagramAccountConnectionStatus.DISCONNECTED);
                entityManager.persist(connection);

                ProfileSocial social = entityManager.find(ProfileSocial.class, connection.getSocial().getUid());
                social.setStatus(ProfileSocialStatus.DISCONNECTED);
                entityManager.persist(social);
            });
        } catch (InstagramException e) {
            logger.error("InstagramException: {}", e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        WorkerRunner.start(InstagramWorker.class,
                new DatabaseModule()
        );
    }
}