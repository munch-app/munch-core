package app.munch.api;

import app.munch.image.ImageUtils;
import app.munch.model.Account;
import app.munch.model.Image;
import app.munch.model.ImageSource;
import app.munch.model.Profile;
import dev.fuxing.err.ConflictException;
import dev.fuxing.err.NotFoundException;
import dev.fuxing.err.UnauthorizedException;
import dev.fuxing.err.ValidationException;
import dev.fuxing.jpa.EntityStream;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;
import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.utils.KeyUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.HibernateException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

import javax.inject.Inject;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 2019-08-12
 * Time: 05:47
 */
public final class ImageService extends DataService {
    private static final MultipartConfigElement MULTIPART_CONFIG = new MultipartConfigElement("/temp");
    private static final String BUCKET = "mh0";

    private final S3Client s3Client;

    @Inject
    ImageService(TransactionProvider provider, S3Client s3Client) {
        super(provider);
        this.s3Client = s3Client;
    }

    @Override
    public void route() {
        PATH("/me/images", () -> {
            GET("", this::list);
            POST("", this::post);

            GET("/:id", this::get);
            DELETE("/:id", this::delete);
        });
    }

    public TransportList list(TransportContext ctx) {
        ApiRequest request = ctx.get(ApiRequest.class);
        @NotNull String accountId = request.getAccountId();

        final int size = ctx.querySize(20, 50);
        final ImageSource source = ctx.queryEnum("source", ImageSource.class, null);

        TransportCursor cursor = ctx.queryCursor();
        final Long createdAt = cursor.getLong("createdAt");
        final String cursorId = cursor.get("id");

        return provider.reduce(true, entityManager -> {
            Account account = entityManager.find(Account.class, accountId);
            if (account == null) throw new UnauthorizedException();

            return EntityStream.of(() -> {
                if (createdAt != null && cursorId != null) {
                    return entityManager.createQuery("FROM Image " +
                            "WHERE profile.id = :profileId AND (:source IS NULL OR source = :source) " +
                            "AND (createdAt < :createdAt OR (createdAt = :createdAt AND id < :cursorId)) " +
                            "ORDER BY createdAt DESC, id desc ", Image.class)
                            .setParameter("profileId", account.getProfile().getId())
                            .setParameter("source", source)
                            .setParameter("createdAt", new Date(createdAt))
                            .setParameter("cursorId", cursorId)
                            .setMaxResults(size)
                            .getResultList();
                }

                return entityManager.createQuery("FROM Image " +
                        "WHERE profile.id = :profileId AND (:source IS NULL OR source = :source)" +
                        "ORDER BY createdAt DESC, id desc  ", Image.class)
                        .setParameter("profileId", account.getProfile().getId())
                        .setParameter("source", source)
                        .setMaxResults(size)
                        .getResultList();
            }).cursor(size, (image, builder) -> {
                builder.put("createdAt", image.getCreatedAt().getTime());
                builder.put("id", image.getId());
            }).asTransportList();
        });
    }

    public Image post(TransportContext ctx) throws IOException, ServletException {
        ctx.request().attribute("org.eclipse.jetty.multipartConfig", MULTIPART_CONFIG);
        Part part = ctx.request().raw().getPart("file");
        ImageSource source = ImageSource.fromValue(ctx.request().raw().getParameter("source"));

        File file = File.createTempFile(RandomStringUtils.randomAlphanumeric(30),
                "." + FilenameUtils.getExtension(part.getName()));

        if (part.getSize() > 15_000_000) {
            throw new ValidationException("file", "File must be below 15MB.");
        }

        if (source == null || source == ImageSource.UNKNOWN_TO_SDK_VERSION) {
            throw new ValidationException("source", "Source is not valid.");
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
                ApiRequest request = ctx.get(ApiRequest.class);
                @NotNull String accountId = request.getAccountId();

                Profile profile = entityManager.createQuery("SELECT profile from Account WHERE id = :id", Profile.class)
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

    public Image get(TransportContext ctx) {
        final String id = ctx.pathString("id");

        return provider.reduce(true, entityManager -> {
            return entityManager.find(Image.class, id);
        });
    }

    public Image delete(TransportContext ctx) {
        final String id = ctx.pathString("id");

        return provider.reduce(entityManager -> {
            Image image = entityManager.find(Image.class, id);
            if (image == null) throw new NotFoundException();

            try {
                entityManager.remove(image);
                return image;
            } catch (HibernateException e) {
                throw new ConflictException(e.getMessage());
            }
        });
    }
}
