package munch.images.database;

import org.apache.commons.lang3.StringUtils;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;

/**
 * Created By: Fuxing Loh
 * Date: 1/6/2017
 * Time: 7:13 PM
 * Project: munch-core
 */
public final class ImageUtils {
    private static final TikaConfig tika = TikaConfig.getDefaultConfig();

    /**
     * @param contentType Content-Type
     * @return to extension; e.g. png/jpg
     */
    public static String getExtension(String contentType) {
        try {
            MimeType mimeType = tika.getMimeRepository().forName(contentType);
            String extension = mimeType.getExtension();
            if (StringUtils.isEmpty(extension)) throw new RuntimeException("Extension cannot be empty.");
            return extension;
        } catch (MimeTypeException e) {
            throw new RuntimeException(e);
        }
    }
}
