package munch.api.place;

import app.munch.api.ApiRequest;
import munch.api.ApiService;
import munch.file.Image;
import munch.file.ImageClient;
import munch.file.ImageMeta;
import munch.restful.core.JsonUtils;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;
import munch.suggest.SuggestClient;
import munch.suggest.SuggestPlaceEdit;
import munch.suggest.change.Change;
import munch.suggest.change.ChangeImage;
import munch.user.client.UserProfileClient;
import munch.user.data.UserProfile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 2019-03-13
 * Time: 16:25
 * Project: munch-core
 */
@Singleton
public final class PlaceSuggestService extends ApiService {
    private static final Logger logger = LoggerFactory.getLogger(PlaceSuggestService.class);

    private final MultipartConfigElement multipartConfig = new MultipartConfigElement("/temp");
    private final SuggestClient.UserClient suggestClient;
    private final ImageClient imageClient;

    private final UserProfileClient userProfileClient;

    @Inject
    public PlaceSuggestService(SuggestClient suggestClient, ImageClient imageClient, UserProfileClient userProfileClient) {
        this.suggestClient = suggestClient.newUserClient("munch-core");
        this.imageClient = imageClient;
        this.userProfileClient = userProfileClient;
    }

    @Override
    public void route() {
        PATH("/places/:placeId", () -> {
            POST("/suggest", "application/json", this::postJson);
            POST("/suggest/image", "multipart/form-data", this::postImage);
            POST("/suggest/multipart", "multipart/form-data", this::postForm);
        });
    }

    public JsonResult postJson(JsonCall call, ApiRequest request) {
        SuggestPlaceEdit place = SuggestPlaceEdit.parse(call.bodyAsJson());
        String placeId = getPlaceId(call);


        UserProfile profile = userProfileClient.get(request.getAccountId());
        Objects.requireNonNull(profile);
        suggestClient.suggest(profile.getUserId(), profile.getName(), profile.getEmail(),
                placeId, place.getChanges()
        );
        return JsonResult.ok();
    }

    public JsonResult postImage(JsonCall call, ApiRequest request) throws IOException, ServletException {
        call.request().attribute("org.eclipse.jetty.multipartConfig", multipartConfig);

        SuggestPlaceEdit place = new SuggestPlaceEdit();
        String placeId = getPlaceId(call);

        ChangeImage.FlagAs flagAs = EnumUtils.getEnum(ChangeImage.FlagAs.class, call.request().queryParams("flagAs"));
        Part part = call.request().raw().getPart("image");

        ChangeImage image = new ChangeImage();
        image.setOperation(Change.Operation.Append);
        image.setFlagAs(flagAs);
        image.setImage(download(part, request));
        place.setChanges(List.of(image));

        // TODO(fuxing): Not compatible with suggest service
        UserProfile profile = userProfileClient.get(request.getAccountId());
        Objects.requireNonNull(profile);
        suggestClient.suggest(profile.getUserId(), profile.getName(), profile.getEmail(),
                placeId, place.getChanges()
        );
        return JsonResult.ok();
    }

    public JsonResult postForm(JsonCall call, ApiRequest request) throws IOException, ServletException {
        call.request().attribute("org.eclipse.jetty.multipartConfig", multipartConfig);

        SuggestPlaceEdit place = SuggestPlaceEdit.parse(JsonUtils.readTree(call.request().queryParams("json")));
        String placeId = getPlaceId(call);

        for (Part part : call.request().raw().getParts()) {
            if (!part.getName().equals("images")) continue;

            ChangeImage image = new ChangeImage();
            image.setOperation(Change.Operation.Append);
            image.setFlagAs(ChangeImage.FlagAs.TypeFood);
            image.setImage(download(part, request));
            place.getChanges().add(image);
        }

        UserProfile profile = userProfileClient.get(request.getAccountId());
        Objects.requireNonNull(profile);
        suggestClient.suggest(profile.getUserId(), profile.getName(), profile.getEmail(),
                placeId, place.getChanges()
        );
        return JsonResult.ok();
    }

    @SuppressWarnings("Duplicates")
    private ImageMeta download(Part part, ApiRequest request) throws IOException {
        UserProfile userProfile = userProfileClient.get(request.getAccountId());
        Objects.requireNonNull(userProfile);

        Image.Profile profile = new Image.Profile();
        profile.setId(request.getAccountId());
        profile.setName(userProfile.getName());
        profile.setType("munch-user");

        File file = null;

        try {
            file = File.createTempFile(RandomStringUtils.randomAlphanumeric(30), part.getName());
            try (InputStream inputStream = part.getInputStream()) {
                FileUtils.copyInputStreamToFile(inputStream, file);
            }

            return imageClient.upload(file, null, profile);
        } finally {
            if (file != null) FileUtils.deleteQuietly(file);
        }
    }

    @Nullable
    private static String getPlaceId(JsonCall call) {
        String placeId = call.pathString("placeId");
        if (placeId.equals("_")) return null;
        return placeId;
    }
}
