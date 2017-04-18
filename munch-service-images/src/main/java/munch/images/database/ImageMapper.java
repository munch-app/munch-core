package munch.images.database;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.utils.file.ContentTypeError;
import com.munch.utils.file.FileMapper;
import com.munch.utils.file.FileTypeUtils;
import com.squareup.pollexor.Thumbor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 10:38 PM
 * Project: munch-core
 */
@Singleton
public class ImageMapper {
    private static final Logger logger = LoggerFactory.getLogger(ImageMapper.class);

    private final FileMapper fileMapper;
    private final ImageDatabase database;

    private final Thumbor thumbor;
    private final TikaConfig tika;

    @Inject
    public ImageMapper(FileMapper fileMapper, ImageDatabase database, Thumbor thumbor) {
        this.fileMapper = fileMapper;
        this.database = database;
        this.thumbor = thumbor;
        this.tika = TikaConfig.getDefaultConfig();
    }

    public Image create(String key, String fileName, File file, TypeDescription... types) throws ContentTypeError, IOException, MimeTypeException {
        Image image = new Image();
        image.setKey(key);
        image.setTypes(new ArrayList<>());

        // Add description types
        for (TypeDescription description : types) {
            Image.Type type = new Image.Type();
            type.setType(description);
            image.getTypes().add(type);
        }

        // Original image type
        Image.Type type = new Image.Type();
        type.setType(TypeDescription.Original);
        type.setKey(randomImageKey(fileName, file));
        fileMapper.put(type.getKey(), file);

        // Persist image, alt put function.
        put(image);
        return image;
    }

    /**
     * TODO doc
     * @param image image object
     */
    public void put(Image image) throws IOException, ContentTypeError, MimeTypeException {
        //noinspection ConstantConditions, because it must exist for all put operations
        Image.Type originalType = image.getTypes().stream()
                .filter(type -> type.getType() == TypeDescription.Original)
                .findFirst().get();
        String originalUrl = fileMapper.getUrl(originalType.getKey());

        for (Image.Type type : image.getTypes()) {
            if (type.getKey() == null) {
                String imageUrl = thumbor.buildImage(originalUrl)
                        .resize(type.getType().getWidth(), type.getType().getHeight())
                        .fitIn().toUrl();
                String imageKey = randomImageKey(originalType.getKey());
                File tempFile = File.createTempFile(imageKey, "");
                FileUtils.copyURLToFile(new URL(imageUrl), tempFile);
                fileMapper.put(imageKey, tempFile);
            }
        }

        // Finally save image
        database.put(image);
    }

    public Image get(String key) {
        return database.get(key);
    }

    public List<Image> get(List<String> keys) {
        return database.get(keys);
    }

    /**
     * Delete all version of the image first
     * If operation incomplete, half the image deletion will be processed
     *
     * @param key image group key
     */
    public void delete(String key) throws IOException, ContentTypeError, MimeTypeException {
        Image image = get(key);
        try {
            // Iterate through all type and delete image
            for (Image.Type type : image.getTypes()) {
                fileMapper.remove(type.getKey());
                image.getTypes().remove(type);
            }
            database.delete(key);
        } catch (Exception e) {
            logger.warn("Failed delete operation, saving delete progress for key: {}", key);
            put(image);
            throw e;
        }
    }

    private String randomImageKey(String fileName, File file) throws ContentTypeError, MimeTypeException {
        MimeType mimeType = tika.getMimeRepository().forName(FileTypeUtils.getContentType(fileName, file));
        return RandomStringUtils.randomAlphanumeric(32) + "." + mimeType.getExtension();
    }

    private String randomImageKey(String fileName) throws ContentTypeError, MimeTypeException {
        MimeType mimeType = tika.getMimeRepository().forName(FileTypeUtils.getContentType(fileName));
        return RandomStringUtils.randomAlphanumeric(32) + "." + mimeType.getExtension();
    }
}
