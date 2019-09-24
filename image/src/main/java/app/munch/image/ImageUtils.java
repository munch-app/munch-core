package app.munch.image;

import com.google.common.collect.ImmutableSet;
import com.google.common.io.Files;
import dev.fuxing.utils.KeyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 21/4/2018
 * Time: 8:32 AM
 * Project: v22-file
 */
public final class ImageUtils {
    private static final Logger logger = LoggerFactory.getLogger(ImageUtils.class);

    private static final TikaConfig TIKA = TikaConfig.getDefaultConfig();
    private static final Set<String> CONTENT_TYPES = ImmutableSet.of("image/jpeg", "image/png", "image/webp");

    /**
     * @param url public url
     * @return SHA 256 url, for uploaded url
     */
    public static String sha256Url(String url) {
        return KeyUtils.sha256Base64(url);
    }

    /**
     * @return UUID encoded as base 64, url safe
     */
    public static String randomId() {
        return KeyUtils.randomUUIDBase64();
    }

    /**
     * @param fileId      id of image file
     * @param contentType content type
     * @param width       of image
     * @param height      of image
     * @return sized key = fileId_{Width}x{Height}.extension
     */
    public static String createSizedKey(String fileId, String contentType, int width, int height) {
        String extension = getExtension(contentType);
        return fileId + "_" + width + "x" + height + extension;
    }

    public static String createOriginalKey(String fileId, String contentType) {
        String extension = getExtension(contentType);
        return fileId + extension;
    }

    /**
     * @param path path where image reside
     * @return dimension of image
     */
    public static Dimension getImageDimension(String path) {
        Dimension result = null;
        String suffix = Files.getFileExtension(path);
        Iterator<ImageReader> iterator = ImageIO.getImageReadersBySuffix(suffix);
        if (iterator.hasNext()) {
            ImageReader reader = iterator.next();
            try {
                ImageInputStream stream = new FileImageInputStream(new File(path));
                reader.setInput(stream);
                int width = reader.getWidth(reader.getMinIndex());
                int height = reader.getHeight(reader.getMinIndex());
                result = new Dimension(width, height);
            } catch (IOException e) {
                logger.warn("Image Dimension Read Error", e);
            } finally {
                reader.dispose();
            }
        } else {
            logger.warn("No reader found for given format: {}", suffix);
        }
        return result;
    }

    /**
     * @param file        to read dimension from
     * @param contentType to prepare reader
     * @return dimension of image
     */
    public static Dimension getImageDimension(File file, String contentType) throws ImageParseException {
        Dimension result = null;
        Iterator<ImageReader> iterator = ImageIO.getImageReadersByMIMEType(contentType);
        if (iterator.hasNext()) {
            ImageReader reader = iterator.next();
            try {
                ImageInputStream stream = new FileImageInputStream(file);
                reader.setInput(stream);
                int width = reader.getWidth(reader.getMinIndex());
                int height = reader.getHeight(reader.getMinIndex());
                result = new Dimension(width, height);
            } catch (IOException e) {
                logger.warn("Image Dimension Read Error", e);
                throw new ImageParseException(e.getMessage());
            } finally {
                reader.dispose();
            }
        } else {
            logger.warn("No reader found for given format: {}", contentType);
        }
        return result;
    }

    /**
     * @param contentType Content-Type
     * @return to extension; e.g. .png or .jpg
     */
    public static String getExtension(String contentType) {
        try {
            MimeType mimeType = TIKA.getMimeRepository().forName(contentType);
            String extension = mimeType.getExtension();
            if (StringUtils.isEmpty(extension)) {
                throw new RuntimeException("Extension cannot be empty. (" + contentType + ")");
            }
            return extension;
        } catch (MimeTypeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param contentType content type to resolveContentType
     * @return content type
     * @throws InvalidFileException if failed
     */
    public static String resolveContentType(String contentType) throws InvalidFileException {
        if (contentType.contains(";")) contentType = contentType.split(";")[0];
        if (!CONTENT_TYPES.contains(contentType))
            throw new InvalidFileException("Uploaded content type must be image. current: (" + contentType + ")");
        return contentType;
    }

    /**
     * @param contentType content type given
     * @param fileName    file name
     * @param file        actual file
     * @return content type
     * @throws ContentTypeException if failed
     */
    public static String resolveContentType(String contentType, String fileName, File file) throws ContentTypeException, InvalidFileException {
        // Check contentType, if is octet then find actual
        if (contentType.equals("application/octet-stream")) {
            contentType = ContentTypeUtils.getContentType(fileName, file);
        }
        return resolveContentType(contentType);
    }

    /**
     * @param contentType content type given
     * @param file        actual file
     * @return content type
     * @throws ContentTypeException if failed
     */
    public static String resolveContentType(String contentType, File file) throws ContentTypeException, InvalidFileException {
        // Check contentType, if is octet then find actual
        if (contentType.equals("application/octet-stream")) {
            contentType = ContentTypeUtils.getContentType(file);
        }
        return resolveContentType(contentType);
    }

    /**
     * @param file image file
     * @return content type
     * @throws ContentTypeException if failed
     */
    public static String getRealContentType(File file) throws ContentTypeException {
        return ContentTypeUtils.getContentType(file);
    }
}
