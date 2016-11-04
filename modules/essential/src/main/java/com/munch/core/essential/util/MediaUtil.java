package com.munch.core.essential.util;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

/**
 * Created by Fuxing
 * Date: 11/7/2015
 * Time: 1:54 AM
 * Project: 3Lines
 */
public class MediaUtil {

    public static Dimension getImageSizeWithFallback(URL url) throws IOException {
        try {
            return getImageSize(url);
        } catch (Exception ignored) {
            // If failed to get image size, try full download
            return getImageSizeFullDownload(url);
        }
    }

    /**
     * About 6 - 7 times faster or maybe more, I don't know
     *
     * @param url image url
     * @return Dimension
     * @throws IOException
     */
    public static Dimension getImageSize(URL url) throws IOException {
        // Create input from url
        try (InputStream inputStream = url.openStream()) {
            // Pass input stream to image stream
            try (ImageInputStream imageStream = ImageIO.createImageInputStream(inputStream)) {
                final Iterator<ImageReader> readers = ImageIO.getImageReaders(imageStream);
                if (readers.hasNext()) {
                    ImageReader reader = readers.next();
                    try {
                        reader.setInput(imageStream);
                        return new Dimension(reader.getWidth(0), reader.getHeight(0));
                    } finally {
                        reader.dispose();
                    }
                }
            }
        }
        // Throws errors if cannot find
        throw new IOException("Unable to find image information from image reader");
    }

    public static Dimension getImageSizeFullDownload(URL url) throws IOException {
        BufferedImage image = ImageIO.read(url);
        return new Dimension(image.getWidth(), image.getHeight());
    }

    public static String getMimeType(File file) {
        return new MimetypesFileTypeMap().getContentType(file);
    }
}