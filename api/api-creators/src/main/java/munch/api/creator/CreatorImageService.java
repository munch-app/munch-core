package munch.api.creator;

import munch.file.Image;
import munch.file.ImageClient;
import munch.file.ImageMeta;
import munch.restful.server.JsonCall;
import munch.user.client.CreatorProfileClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by: Fuxing
 * Date: 2019-02-07
 * Time: 17:21
 * Project: munch-core
 */
@Singleton
public final class CreatorImageService extends AbstractCreatorService {
    private final MultipartConfigElement multipartConfig = new MultipartConfigElement("/temp");

    private final ImageClient imageClient;
    private final CreatorProfileClient profileClient;

    @Inject
    public CreatorImageService(ImageClient imageClient, CreatorProfileClient profileClient) {
        this.imageClient = imageClient;
        this.profileClient = profileClient;
    }

    @Override
    public void route() {
        AUTHENTICATED("/creators/:creatorId/contents/*/images", () -> {
            POST("", this::post);
        });
    }

    public ImageMeta post(JsonCall call) throws IOException, ServletException {
        Image.Profile profile = new Image.Profile();
        profile.setId("Temporary");
        profile.setName("Temporary");
        profile.setType("munch-user-creator");


        call.request().attribute("org.eclipse.jetty.multipartConfig", multipartConfig);
        File file = null;

        try {
            Part part = call.request().raw().getPart("file");
            file = File.createTempFile(RandomStringUtils.randomAlphanumeric(30), part.getName());
            try (InputStream inputStream = part.getInputStream()) {
                FileUtils.copyInputStreamToFile(inputStream, file);
            }

            return imageClient.upload(file, null, profile);
        } finally {
            if (file != null) FileUtils.deleteQuietly(file);
        }
    }
}
