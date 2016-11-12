package com.munch.core.essential.file;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tika.Tika;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by: Fuxing
 * Date: 27/10/2016
 * Time: 1:55 PM
 * Project: essential
 */
public class FileMapperTest {

    Tika tika = new Tika();

    @Test
    public void getFileType() throws Exception {
        File file = downloadFile(new URL("https://i.kinja-img.com/gawker-media/image/upload/s--482hkXRF--/c_scale,fl_progressive,q_80,w_800/in9kpbgj1zpmp1ni5pjn.png"));

        System.out.println(tika.detect(file));
        System.out.println(file.getName());
        System.out.println(tika.detect(file.getName()));
        System.out.println(tika.detect("filename"));
    }

    private File downloadFile(URL url) throws IOException {
        String extension = FilenameUtils.getExtension(url.getPath());
        File temp = File.createTempFile(RandomStringUtils.randomAlphabetic(30), "." + extension);
        FileUtils.copyURLToFile(url, temp);
        return temp;
    }

}