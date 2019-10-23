package app.munch.worker;

import app.munch.database.DatabaseModule;
import app.munch.image.ImageModule;
import app.munch.model.ProfileMedia;
import app.munch.model.ProfileMediaStatus;
import app.munch.model.WorkerTask;
import dev.fuxing.jpa.TransactionProvider;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.List;

/**
 * @author Fuxing Loh
 * @since 7/10/19 at 5:00 pm
 */
@Singleton
public final class MediaWorker implements WorkerRunner {
    private static final Logger logger = LoggerFactory.getLogger(MediaWorker.class);

    private final TransactionProvider provider;
    private final MediaAnalyser analyser;

    @Inject
    MediaWorker(TransactionProvider provider, MediaAnalyser analyser) {
        this.provider = provider;
        this.analyser = analyser;
    }

    @Override
    public String groupUid() {
        return "01dpkgbwqqmrk9ht5bzhw2jthh";
    }

    @Override
    public void run(WorkerTask task) throws IOException {
        do {
            List<ProfileMedia> medias = provider.reduce(true, entityManager -> {
                List<ProfileMedia> list = entityManager.createQuery(
                        "FROM ProfileMedia WHERE status = :status ORDER BY createdAt DESC",
                        ProfileMedia.class)
                        .setParameter("status", ProfileMediaStatus.PENDING)
                        .setMaxResults(10)
                        .getResultList();

                list.forEach(media -> {
                    Hibernate.initialize(media.getImages());
                });
                return list;
            });

            for (ProfileMedia media : medias) {
                analyser.analyse(media);
            }
        } while (true);
    }

    public static void main(String[] args) {
        WorkerRunner.start(MediaWorker.class,
                new DatabaseModule(), new ImageModule()
        );
    }
}
