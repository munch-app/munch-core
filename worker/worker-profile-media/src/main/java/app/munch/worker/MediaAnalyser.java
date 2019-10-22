package app.munch.worker;

import app.munch.model.Image;
import app.munch.model.ProfileMedia;
import app.munch.model.ProfileMediaStatus;
import app.munch.vision.VisionClient;
import app.munch.vision.VisionResult;
import app.munch.vision.VisionResultType;
import dev.fuxing.jpa.TransactionProvider;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;

/**
 * @author Fuxing Loh
 * @since 2019-10-18 at 23:17
 */
@Singleton
public final class MediaAnalyser {
    private static final Logger logger = LoggerFactory.getLogger(MediaAnalyser.class);

    private final TransactionProvider provider;
    private final VisionClient visionClient;
    private final S3Client s3Client;

    @Inject
    public MediaAnalyser(TransactionProvider provider, VisionClient visionClient, S3Client s3Client) {
        this.provider = provider;
        this.visionClient = visionClient;
        this.s3Client = s3Client;
    }

    public void analyse(ProfileMedia media) throws IOException {
        if (media.getImages().isEmpty()) {
            complete(media, ProfileMediaStatus.HIDDEN);
            return;
        }

        File file = null;
        try {
            file = getFile(media.getImages().get(0));

            VisionResult result = visionClient.post(file);
            logger.info("{}, returns {}", media.getId(), result.getName());

            if (result.getName() == VisionResultType.food) {
                complete(media, ProfileMediaStatus.PUBLIC);
            } else {
                complete(media, ProfileMediaStatus.HIDDEN);
            }
        } finally {
            FileUtils.deleteQuietly(file);
        }
    }

    /**
     * @param image to download from s3
     * @return File
     * @throws IOException unable to create temp file, upload to donwload file s3
     */
    private File getFile(Image image) throws IOException {
        String key = RandomStringUtils.randomAlphanumeric(30);
        File file = File.createTempFile(key, image.getExt());

        s3Client.getObject(builder -> {
            builder.bucket(image.getBucket());
            builder.key(image.getUid() + image.getExt());
        }, ResponseTransformer.toFile(file));
        return file;
    }


    private void complete(ProfileMedia media, ProfileMediaStatus status) {
        provider.with(entityManager -> {
            ProfileMedia entity = entityManager.find(ProfileMedia.class, media.getId());
            entity.setStatus(status);
            entityManager.persist(entity);
        });
    }
}
