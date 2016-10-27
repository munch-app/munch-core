package com.munch.core.essential.file;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import javax.activation.MimetypesFileTypeMap;
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

    @Test
    public void getFileType() throws Exception {
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        File file = downloadFile(new URL("https://i.kinja-img.com/gawker-media/image/upload/s--482hkXRF--/c_scale,fl_progressive,q_80,w_800/in9kpbgj1zpmp1ni5pjn.png"));
        System.out.println(mimeTypesMap.getContentType("https://i.kinja-img.com/gawker-media/image/upload/s--482hkXRF--/c_scale,fl_progressive,q_80,w_800/in9kpbgj1zpmp1ni5pjn.png"));
    }

    private File downloadFile(URL url) throws IOException {
        File temp = File.createTempFile(RandomStringUtils.randomAlphabetic(30), "tmp");
        FileUtils.copyURLToFile(url, temp);
        return temp;
    }
}