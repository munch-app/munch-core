package app.munch.migration;

import app.munch.image.ImageUtils;
import app.munch.model.Image;
import app.munch.model.ImageSource;
import app.munch.model.PlaceImage;
import app.munch.model.Profile;
import dev.fuxing.utils.KeyUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

import javax.annotation.Nullable;
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
public final class ImageMigration {
    private static final String BUCKET = "mh0";
    private final S3Client s3Client;

    private EntityMigrationTable entityMigrationTable;

    @Inject
    ImageMigration(S3Client s3Client, EntityMigrationTable entityMigrationTable) {
        this.s3Client = s3Client;
        this.entityMigrationTable = entityMigrationTable;
    }

    @Nullable
    public Image findImage(EntityManager entityManager, String imageId) {
        String uid = entityMigrationTable.getUID("PlaceImage", imageId);
        if (uid != null) {
            PlaceImage placeImage = entityManager.find(PlaceImage.class, uid);
            return placeImage.getImage();
        }

        uid = entityMigrationTable.getUID("Image", imageId);
        if (uid != null) {
            return entityManager.find(Image.class, uid);
        }

        return null;
    }

    public Image post(EntityManager entityManager, String imageId, String url) throws IOException {
        // Find from EntityMigration Table first
        Image image = findImage(entityManager, imageId);
        if (image != null) return image;

        Profile profile = entityManager.find(Profile.class, Profile.COMPAT_ID);
        image = post(entityManager, ImageSource.LIBRARY, profile, url);

        entityMigrationTable.putUID("Image", imageId, image.getUid());
        return image;
    }

    public Image post(EntityManager entityManager, String imageId, List<munch.file.Image.Size> sizes) {
        Image image = findImage(entityManager, imageId);
        if (image != null) return image;

        String url = sizes.stream().max(Comparator.comparing(munch.file.Image.Size::getHeight))
                .map(munch.file.Image.Size::getUrl)
                .orElse(null);

        if (url == null) return null;

        try {
            return post(entityManager, imageId, url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Image post(EntityManager entityManager, ImageSource source, Profile profile, String url) throws IOException {
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
