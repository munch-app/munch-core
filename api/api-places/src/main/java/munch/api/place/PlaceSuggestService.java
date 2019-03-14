package munch.api.place;

import munch.api.ApiRequest;
import munch.api.ApiService;
import munch.file.Image;
import munch.file.ImageClient;
import munch.file.ImageMeta;
import munch.restful.core.JsonUtils;
import munch.restful.core.KeyUtils;
import munch.restful.core.exception.ConflictException;
import munch.restful.core.exception.ValidationException;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;
import munch.suggest.SuggestClient;
import munch.suggest.SuggestPlaceEdit;
import munch.suggest.change.ChangeField;
import munch.suggest.change.ChangeFieldImage;
import munch.user.client.UserProfileClient;
import munch.user.data.UserProfile;
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
 * Date: 2019-03-13
 * Time: 16:25
 * Project: munch-core
 */
@Singleton
public final class PlaceSuggestService extends ApiService {
    private final MultipartConfigElement multipartConfig = new MultipartConfigElement("/temp");

    private final SuggestClient suggestClient;
    private final UserProfileClient profileClient;
    private final ImageClient imageClient;

    @Inject
    public PlaceSuggestService(SuggestClient suggestClient, UserProfileClient profileClient, ImageClient imageClient) {
        this.suggestClient = suggestClient;
        this.profileClient = profileClient;
        this.imageClient = imageClient;
    }

    @Override
    public void route() {
        PATH("/places/:placeId", () -> {
            POST("/suggest", "application/json", this::postJson);
            POST("/suggest", "multipart/form-data", this::postForm);
        });
    }

    public JsonResult postJson(JsonCall call, ApiRequest request) {
        SuggestPlaceEdit.Source source = getSource(request);
        SuggestPlaceEdit place = call.bodyAsObject(SuggestPlaceEdit.class);
        place.setPlaceId(call.pathString("placeId"));
        place.setSource(source);

        validate(place);

        // TODO Send Request
        // suggestClient.post(place);
        return JsonResult.ok();
    }

    public JsonResult postForm(JsonCall call, ApiRequest request) throws IOException, ServletException {
        SuggestPlaceEdit.Source source = getSource(request);
        call.request().attribute("org.eclipse.jetty.multipartConfig", multipartConfig);

        SuggestPlaceEdit place = JsonUtils.toObject(call.request().queryParams("json"), SuggestPlaceEdit.class);
        place.setPlaceId(call.pathString("placeId"));
        place.setSource(source);
        validate(place);

        for (Part part : call.request().raw().getParts()) {
            if (!part.getName().equals("images")) continue;
            ChangeFieldImage image = new ChangeFieldImage();
            image.setOperation(ChangeField.Operation.Append);
            image.setFlagAs(ChangeFieldImage.FlagAs.QualityGood);
            image.setImage(download(part, source));
            place.getFields().add(image);
        }

        // TODO Send Request
        // suggestClient.post(place);
        return JsonResult.ok();
    }

    private SuggestPlaceEdit.Source getSource(ApiRequest request) {
        final String userId = request.getUserId();
        UserProfile profile = profileClient.get(userId);
        if (profile == null) throw new ConflictException("Profile not found. (please report this error.)");

        SuggestPlaceEdit.Source source = new SuggestPlaceEdit.Source();
        source.setSystem("munch-core");
        source.setUserId(userId);
        source.setEmail(profile.getEmail());
        source.setName(profile.getName());
        return source;
    }

    @SuppressWarnings("Duplicates")
    private ImageMeta download(Part part, SuggestPlaceEdit.Source source) throws IOException {
        Image.Profile profile = new Image.Profile();
        profile.setId(source.getUserId());
        profile.setName(source.getName());
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

    /**
     * @param place attempt to validate suggest place
     */
    private static void validate(SuggestPlaceEdit place) {
        place.setSuggestId(KeyUtils.randomUUID());
        place.setVersion(SuggestPlaceEdit.CURRENT_VERSION);
        place.setStatus(SuggestPlaceEdit.Status.Pending);

        ValidationException.validate(place);

        place.setSuggestId(null);
        place.setVersion(null);
        place.setStatus(null);
    }
}
