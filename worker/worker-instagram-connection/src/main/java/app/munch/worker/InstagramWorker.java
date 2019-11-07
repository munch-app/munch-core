package app.munch.worker;

import app.munch.database.DatabaseModule;
import app.munch.image.ImageModule;
import app.munch.model.*;
import com.instagram.err.InstagramAccessException;
import com.instagram.err.InstagramException;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.utils.SleepUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.Date;
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
    public void run(WorkerTask workerTask) {
        do {
            InstagramAccountConnectionTask task = acquireTask();
            if (task != null) {
                connect(task);
                logger.info("Completed: {}", task.getConnection().getSocial().getName());
                continue;
            }

            SleepUtils.sleep(Duration.ofMinutes(3));
        } while (true);
    }

    /**
     * @return Acquired ConnectionTask
     */
    @Nullable
    private InstagramAccountConnectionTask acquireTask() {
        InstagramAccountConnectionTask acquired = provider.reduce(entityManager -> {
            InstagramAccountConnection connection = acquireConnection(entityManager);
            if (connection == null) {
                return null;
            }

            InstagramAccountConnectionTask task = new InstagramAccountConnectionTask();
            task.setConnection(connection);
            entityManager.persist(task);

            connection.setConnectedAt(new Timestamp(System.currentTimeMillis()));
            entityManager.persist(task);
            return task;
        });

        if (acquired == null) {
            return null;
        }

        // Acquired task, wait 10 seconds to reconfirm no one else did.
        SleepUtils.sleep(Duration.ofSeconds(10));

        return provider.reduce(true, entityManager -> {
            List<InstagramAccountConnectionTask> tasks = entityManager.createQuery("FROM InstagramAccountConnectionTask " +
                    "ORDER BY createdAt DESC ", InstagramAccountConnectionTask.class)
                    .setMaxResults(1)
                    .getResultList();

            InstagramAccountConnectionTask latest = tasks.get(0);
            if (latest.getUid().equals(acquired.getUid())) {
                Hibernate.initialize(latest.getConnection());
                Hibernate.initialize(latest.getConnection().getSocial());
                Hibernate.initialize(latest.getConnection().getSocial().getProfile());
                return latest;
            }

            // Someone other worker took latest spot.
            return null;
        });
    }

    @Nullable
    private InstagramAccountConnection acquireConnection(EntityManager entityManager) {
        List<InstagramAccountConnection> list = entityManager.createQuery("FROM InstagramAccountConnection " +
                "WHERE status = :status AND connectedAt IS NULL", InstagramAccountConnection.class)
                .setParameter("status", InstagramAccountConnectionStatus.CONNECTED)
                .setMaxResults(1)
                .getResultList();

        if (!list.isEmpty()) {
            return list.get(0);
        }

        list = entityManager.createQuery("FROM InstagramAccountConnection " +
                "WHERE status = :status AND connectedAt < :at " +
                "ORDER BY connectedAt ASC", InstagramAccountConnection.class)
                .setParameter("status", InstagramAccountConnectionStatus.CONNECTED)
                .setParameter("at", new Date(System.currentTimeMillis() - Duration.ofMinutes(75).toMillis()))
                .setMaxResults(1)
                .getResultList();

        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    private void connect(InstagramAccountConnectionTask task) {
        try {
            dataRiver.open(task);
        } catch (InstagramAccessException e) {
            disconnect(task);
        } catch (InstagramException e) {
            logger.error("InstagramException: {}", e.getMessage(), e);
        }
    }

    private void disconnect(InstagramAccountConnectionTask task) {
        String uid = task.getConnection().getUid();
        provider.with(entityManager -> {
            InstagramAccountConnection connection = entityManager.find(InstagramAccountConnection.class, uid);
            connection.setStatus(InstagramAccountConnectionStatus.DISCONNECTED);
            entityManager.persist(connection);

            ProfileSocial social = entityManager.find(ProfileSocial.class, connection.getSocial().getUid());
            social.setStatus(ProfileSocialStatus.DISCONNECTED);
            entityManager.persist(social);
        });
    }

    public static void main(String[] args) {
        WorkerRunner.start(InstagramWorker.class,
                new DatabaseModule(), new ImageModule()
        );
    }
}
