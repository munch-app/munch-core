package com.munch.core.struct.util;

import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created By: Fuxing Loh
 * Date: 23/8/2016
 * Time: 8:01 PM
 * Project: struct
 */
public class FileValidation {

    /**
     * @param file        File to validate
     * @param maxFileSize max size of file allowed
     * @return Whether file is below or equal to max file size given
     */
    public static boolean validateSize(File file, long maxFileSize) {
        return FileUtils.sizeOf(file) <= maxFileSize;
    }

    /**
     * @param file file to validate
     * @return Whether the file is Image
     * @throws IOException read error
     */
    public static boolean validateImage(File file) throws IOException {
        try (InputStream input = new FileInputStream(file)) {
            // It's an image (only BMP, GIF, JPG and PNG are recognized).
            return ImageIO.read(input) != null;
        }
    }
}
