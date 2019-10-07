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
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * ImageUploadClient to upload image into Munch ecosystem.
 *
 * @author Fuxing Loh
 * @since 16/8/19 at 10:07 pm
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

    /**
     * @param file          to upload
     * @param source        of where the image is coming from
     * @param profileMapper to get Profile for the Image
     * @return Image model created
     */
    public Image upload(File file, ImageSource source, Function<EntityManager, Profile> profileMapper) {
        return upload(file, source, profileMapper, null);
    }

    /**
     * @param file          to upload
     * @param source        of where the image is coming from
     * @param profileMapper to get Profile for the Image
     * @param imageConsumer to inject data into Image before persisting
     * @return Image model created
     */
    public Image upload(File file, ImageSource source, Function<EntityManager, Profile> profileMapper, Consumer<Image> imageConsumer) {
        Objects.requireNonNull(file);
        Objects.requireNonNull(source);
        Objects.requireNonNull(profileMapper);

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

            if (imageConsumer != null) {
                imageConsumer.accept(image);
            }

            provider.with(entityManager -> {
                Profile profile = profileMapper.apply(entityManager);
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
