package app.munch.api.migration;

import app.munch.image.ImageUtils;
import app.munch.model.Image;
import app.munch.model.ImageSource;
import app.munch.model.Profile;
import dev.fuxing.utils.KeyUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 27/8/19
 * Time: 10:21 PM
 * Project: munch-core
 */
@SuppressWarnings("DuplicatedCode")
@Singleton
public final class ImageMigrationManager {
    private static final String BUCKET = "mh0";
    private final S3Client s3Client;

    @Inject
    ImageMigrationManager(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public Image post(EntityManager entityManager, String url) throws IOException {
        Profile profile = entityManager.find(Profile.class, Profile.COMPAT_ID);
        return post(entityManager, ImageSource.LIBRARY, profile, url);
    }

    public Image post(EntityManager entityManager, List<munch.file.Image.Size> sizes) {
        String url = sizes.stream().max(Comparator.comparing(munch.file.Image.Size::getHeight))
                .map(munch.file.Image.Size::getUrl)
                .orElse(null);

        if (url == null) return null;

        try {
            return post(entityManager, url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Image post(EntityManager entityManager, ImageSource source, Profile profile, String url) throws IOException {
        File file = File.createTempFile(RandomStringUtils.randomAlphanumeric(30),
                "." + FilenameUtils.getExtension(url));

        try {
            FileUtils.copyURLToFile(new URL(url), file);

            String contentType = ImageUtils.getRealContentType(file);
            Dimension dimension = ImageUtils.getImageDimension(file, contentType);

            Image image = new Image();
            image.setBucket(BUCKET);
            image.setUid(KeyUtils.nextULID());
            image.setExt(ImageUtils.getExtension(contentType));

            image.setSource(source);
            image.setWidth((int) dimension.getWidth());
            image.setHeight((int) dimension.getHeight());


            image.setProfile(profile);
            entityManager.persist(image);

            s3Client.putObject(builder -> {
                builder.bucket(BUCKET);
                builder.key(image.getUid() + image.getExt());
                builder.contentType(contentType);
            }, RequestBody.fromFile(file));
            return image;
        } finally {
            FileUtils.deleteQuietly(file);
        }
    }
}
