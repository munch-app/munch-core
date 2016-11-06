package com.munch.core.essential.file;

import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;

import static org.apache.tika.mime.MimeTypes.OCTET_STREAM;

/**
 * Created By: Fuxing Loh
 * Date: 3/11/2016
 * Time: 6:40 PM
 * Project: essential
 */
public class FileMapperUtils {

    private static final Tika tika = new Tika();

    /**
     * Get content type from file name
     *
     * @param filename file name
     * @return content type
     */
    public static String getContentType(String filename) {
        return tika.detect(filename);
    }

    public static String getContentType(File file) throws ContentTypeException {
        try {
            return tika.detect(file);
        } catch (IOException e) {
            throw new ContentTypeException(e);
        }
    }

    public static String getContentType(byte[] bytes) throws ContentTypeException {
        return tika.detect(bytes);
    }

    public static String getContentType(String filename, File file) throws ContentTypeException {
        String type = getContentType(filename);
        if (type.equalsIgnoreCase(OCTET_STREAM)) {
            type = getContentType(file);
        }
        return type;
    }

    public static String getContentType(String filename, byte[] bytes) throws ContentTypeException {
        String type = getContentType(filename);
        if (type.equalsIgnoreCase(OCTET_STREAM)) {
            type = getContentType(bytes);
        }
        return type;
    }

}
