package app.munch.image;

import org.apache.tika.Tika;

import java.io.File;

import static org.apache.tika.mime.MimeTypes.OCTET_STREAM;

/**
 * Created By: Fuxing Loh
 * Date: 3/11/2016
 * Time: 6:40 PM
 * Project: v22-file
 */
public final class ContentTypeUtils {
    private static Tika tika = new Tika();

    /**
     * Get content type from file name alone
     *
     * @param filename file name
     * @return content type
     */
    public static String getContentType(String filename) {
        return tika.detect(filename);
    }

    /**
     * Detects the media type of the given file. The type detection is
     * based on the document content and a potential known file extension.
     *
     * @param file file
     * @return content type
     * @throws ContentTypeException unable to read
     */
    public static String getContentType(File file) throws ContentTypeException {
        try {
            return tika.detect(file);
        } catch (Exception e) {
            throw new ContentTypeException(e);
        }
    }

    /**
     * Detects the media type of the given document. The type detection is
     * based on the first few bytes of a document.
     *
     * @param bytes bytes of file
     * @return content type
     */
    public static String getContentType(byte[] bytes) throws ContentTypeException {
        try {
            return tika.detect(bytes);
        } catch (Exception e) {
            throw new ContentTypeException(e);
        }
    }

    /**
     * Detects the media type of the given file. The type detection is
     * based on the document content and a potential known file extension.
     *
     * @param filename name of file
     * @param file     file
     * @return content type
     * @throws ContentTypeException unable to read
     */
    public static String getContentType(String filename, File file) throws ContentTypeException {
        String type = getContentType(filename);
        if (type.equalsIgnoreCase(OCTET_STREAM)) {
            type = getContentType(file);
        }
        return type;
    }

    /**
     * Detect based on potential known file extension.
     * If unable to then:
     * Detects the media type of the given document. The type detection is
     * based on the first few bytes of a document.
     *
     * @param filename name of file
     * @param bytes    bytes of file
     * @return content type
     * @throws ContentTypeException unable to read
     */
    public static String getContentType(String filename, byte[] bytes) throws ContentTypeException {
        String type = getContentType(filename);
        if (type.equalsIgnoreCase(OCTET_STREAM)) {
            type = getContentType(bytes);
        }
        return type;
    }
}
