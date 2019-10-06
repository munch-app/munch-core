package app.munch.image;

import app.munch.model.Image;
import app.munch.model.ImageSource;
import app.munch.model.Profile;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.utils.KeyUtils;
import org.apache.commons.io.FileUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.awt.*;
import java.io.File;
import java.util.Objects;
import java.util.function.Function;

/**
 * Created by: Fuxing
 * Date: 16/8/19
 * Time: 10:07 pm
 */
@Singleton
public final class ImageUploadClient {
    private final TransactionProvider provider;
    private final S3Client s3Client;

    @Inject
    ImageUploadClient(TransactionProvider provider, S3Client s3Client) {
        this.provider = provider;
        this.s3Client = s3Client;
    }

    public Image upload(File file, ImageSource source, Function<EntityManager, Profile> function) {
        Objects.requireNonNull(file);
        Objects.requireNonNull(source);
        Objects.requireNonNull(function);

        String bucket = Image.resolveBucket(source);

        try {
            String contentType = ImageUtils.getRealContentType(file);
            Dimension dimension = ImageUtils.getImageDimension(file, contentType);

            Image image = new Image();
            image.setBucket(bucket);
            image.setUid(KeyUtils.nextULID());
            image.setExt(ImageUtils.getExtension(contentType));

            image.setSource(source);
            image.setWidth((int) dimension.getWidth());
            image.setHeight((int) dimension.getHeight());

            provider.with(entityManager -> {
                Profile profile = function.apply(entityManager);
                image.setProfile(profile);
                entityManager.persist(image);
            });

            s3Client.putObject(builder -> {
                builder.bucket(bucket);
                builder.key(image.getUid() + image.getExt());
                builder.contentType(contentType);
            }, RequestBody.fromFile(file));
            return image;
        } finally {
            FileUtils.deleteQuietly(file);
        }
    }
}
