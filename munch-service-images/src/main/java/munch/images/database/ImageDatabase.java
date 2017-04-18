package munch.images.database;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.utils.file.AccessControl;
import com.munch.utils.file.ContentTypeError;
import com.munch.utils.file.FileMapper;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 10:40 PM
 * Project: munch-core
 */
@Singleton
@Deprecated
public class ImageDatabase {

    private final FileMapper fileMapper;

    @Inject
    public ImageDatabase(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }


    public void put(String key) throws ContentTypeError {
        fileMapper.put(key, null, AccessControl.PublicRead);
    }

    /**
     * Delete image group from database
     *
     * @param key key of image group
     */
    public void delete(String key) {
        fileMapper.remove(key);
    }

    public String getUrl(String key) {
        return fileMapper.getUrl(key);
    }
}
