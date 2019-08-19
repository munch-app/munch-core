package app.munch.image;

import app.munch.model.Account;
import app.munch.model.Image;
import app.munch.model.ImageSource;
import app.munch.model.Profile;
import dev.fuxing.err.ForbiddenException;
import dev.fuxing.err.UnauthorizedException;
import dev.fuxing.err.ValidationException;
import dev.fuxing.jpa.EntityStream;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;
import dev.fuxing.utils.KeyUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.Part;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 16/8/19
 * Time: 10:07 pm
 */
@Singleton
public final class ImageEntityManager {
    private static final String BUCKET = "mh0";

    private final TransactionProvider provider;
    private final S3Client s3Client;

    @Inject
    ImageEntityManager(TransactionProvider provider, S3Client s3Client) {
        this.provider = provider;
        this.s3Client = s3Client;
    }

    public TransportList list(String accountId, Set<ImageSource> sources, int size, TransportCursor cursor) {
        final Long createdAt = cursor.getLong("createdAt");
        final String cursorId = cursor.get("id");

        return provider.reduce(true, entityManager -> {
            Account account = entityManager.find(Account.class, accountId);
            if (account == null) throw new UnauthorizedException();

            return EntityStream.of(() -> {
                if (createdAt != null && cursorId != null) {
                    if (sources.isEmpty()) {
                        return entityManager.createQuery("FROM Image " +
                                "WHERE profile.id = :profileId " +
                                "AND (createdAt < :createdAt OR (createdAt = :createdAt AND id < :cursorId)) " +
                                "ORDER BY createdAt DESC, id DESC ", Image.class)
                                .setParameter("profileId", account.getProfile().getId())
                                .setParameter("createdAt", new Date(createdAt))
                                .setParameter("cursorId", cursorId)
                                .setMaxResults(size)
                                .getResultList();
                    }

                    return entityManager.createQuery("FROM Image " +
                            "WHERE profile.id = :profileId AND source IN (:sources) " +
                            "AND (createdAt < :createdAt OR (createdAt = :createdAt AND id < :cursorId)) " +
                            "ORDER BY createdAt DESC, id DESC ", Image.class)
                            .setParameter("profileId", account.getProfile().getId())
                            .setParameter("sources", sources)
                            .setParameter("createdAt", new Date(createdAt))
                            .setParameter("cursorId", cursorId)
                            .setMaxResults(size)
                            .getResultList();
                }

                if (sources.isEmpty()) {
                    return entityManager.createQuery("FROM Image " +
                            "WHERE profile.id = :profileId " +
                            "ORDER BY createdAt DESC, id DESC ", Image.class)
                            .setParameter("profileId", account.getProfile().getId())
                            .setMaxResults(size)
                            .getResultList();
                }

                return entityManager.createQuery("FROM Image " +
                        "WHERE profile.id = :profileId AND source IN (:sources) " +
                        "ORDER BY createdAt DESC, id DESC ", Image.class)
                        .setParameter("profileId", account.getProfile().getId())
                        .setParameter("sources", sources)
                        .setMaxResults(size)
                        .getResultList();
            }).cursor(size, (image, builder) -> {
                builder.put("createdAt", image.getCreatedAt().getTime());
                builder.put("id", image.getId());
            }).asTransportList();
        });
    }

    public Image post(String accountId, Part part, ImageSource source) throws IOException {
        File file = File.createTempFile(RandomStringUtils.randomAlphanumeric(30),
                "." + FilenameUtils.getExtension(part.getName()));

        if (part.getSize() > 15_000_000) {
            throw new ValidationException("file", "File must be below 15MB.");
        }

        if (source == null || source == ImageSource.UNKNOWN_TO_SDK_VERSION) {
            throw new ValidationException("source", "Source is not valid.");
        }

        if (accountId == null) {
            throw new ForbiddenException();
        }

        try {
            try (InputStream inputStream = part.getInputStream()) {
                FileUtils.copyInputStreamToFile(inputStream, file);
            }

            String contentType = ImageUtils.getRealContentType(file);
            Dimension dimension = ImageUtils.getImageDimension(file, contentType);

            Image image = new Image();
            image.setBucket(BUCKET);
            image.setId(KeyUtils.nextULID());
            image.setExt(ImageUtils.getExtension(contentType));

            image.setSource(source);
            image.setWidth((int) dimension.getWidth());
            image.setHeight((int) dimension.getHeight());

            provider.with(entityManager -> {
                Profile profile = entityManager.createQuery("SELECT a.profile FROM Account a " +
                        "WHERE a.id = :id", Profile.class)
                        .setParameter("id", accountId)
                        .getSingleResult();
                image.setProfile(profile);
                entityManager.persist(image);
            });

            s3Client.putObject(builder -> {
                builder.bucket(BUCKET);
                builder.key(image.getId() + image.getExt());
                builder.contentType(contentType);
            }, RequestBody.fromFile(file));
            return image;
        } finally {
            FileUtils.deleteQuietly(file);

            try {
                part.delete();
            } catch (Exception ignored) {
            }
        }
    }
}
