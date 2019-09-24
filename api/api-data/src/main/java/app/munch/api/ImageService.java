package app.munch.api;

import app.munch.image.ImageQueryClient;
import app.munch.image.ImageUploadClient;
import app.munch.model.Image;
import app.munch.model.ImageSource;
import dev.fuxing.err.ConflictException;
import dev.fuxing.err.ForbiddenException;
import dev.fuxing.err.NotFoundException;
import dev.fuxing.err.ValidationException;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.TransportList;
import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.transport.service.TransportResult;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.HibernateException;

import javax.inject.Inject;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 2019-08-12
 * Time: 05:47
 */
public final class ImageService extends DataService {
    private static final MultipartConfigElement MULTIPART_CONFIG = new MultipartConfigElement("/temp");

    private final ImageQueryClient queryClient;
    private final ImageUploadClient uploadClient;

    @Inject
    ImageService(TransactionProvider provider, ImageQueryClient queryClient, ImageUploadClient uploadClient) {
        super(provider);
        this.queryClient = queryClient;
        this.uploadClient = uploadClient;
    }

    @Override
    public void route() {
        PATH("/me/images", () -> {
            GET("", this::list);
            POST("", this::post);
            OPTIONS("", this::options);

            GET("/:uid", this::get);
//            DELETE("/:uid", this::delete);
        });
    }

    public TransportList list(TransportContext ctx) {
        String accountId = ctx.get(ApiRequest.class).getAccountId();
        final int size = ctx.querySize(20, 50);
        Set<ImageSource> sources = ImageSource.fromArray(ctx.queryString("sources", null));

        return queryClient.query(accountId, sources, size, ctx.queryCursor());
    }

    public Image post(TransportContext ctx) throws IOException, ServletException {
        ctx.request().attribute("org.eclipse.jetty.multipartConfig", MULTIPART_CONFIG);
        @NotNull String accountId = ctx.get(ApiRequest.class).getAccountId();
        Part part = ctx.request().raw().getPart("file");
        ImageSource source = ImageSource.fromValue(ctx.request().raw().getParameter("source"));

        try {
            return post(accountId, part, source);
        } finally {
            try {
                part.delete();
            } catch (Exception ignored) {
            }
        }
    }

    private Image post(String accountId, Part part, ImageSource source) throws IOException {
        if (part.getSize() > 15_000_000) {
            throw new ValidationException("file", "File must be below 15MB.");
        }

        if (source == null || source == ImageSource.UNKNOWN_TO_SDK_VERSION) {
            throw new ValidationException("source", "Source is not valid.");
        }

        if (accountId == null) {
            throw new ForbiddenException();
        }

        File file = File.createTempFile(RandomStringUtils.randomAlphanumeric(30),
                "." + FilenameUtils.getExtension(part.getName()));

        try (InputStream inputStream = part.getInputStream()) {
            FileUtils.copyInputStreamToFile(inputStream, file);
        }
        return uploadClient.upload(accountId, file, source);
    }

    public TransportResult options(TransportContext ctx) {
        return TransportResult.ok();
    }

    public Image get(TransportContext ctx) {
        final String uid = ctx.pathString("uid");

        return provider.reduce(true, entityManager -> {
            return entityManager.find(Image.class, uid);
        });
    }

    public Image delete(TransportContext ctx) {
        final String uid = ctx.pathString("uid");

        return provider.reduce(entityManager -> {
            Image image = entityManager.find(Image.class, uid);
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
