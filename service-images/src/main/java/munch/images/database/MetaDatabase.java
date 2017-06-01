package munch.images.database;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.utils.block.BlockStoreMapper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created By: Fuxing Loh
 * Date: 1/6/2017
 * Time: 6:13 PM
 * Project: munch-core
 */
@Singleton
public class MetaDatabase {
    private final BlockStoreMapper<?, ?> metaMapper;

    @Inject
    public MetaDatabase(BlockStoreMapper metaMapper) {
        this.metaMapper = metaMapper;
    }

    /**
     * null data will be removed from list
     *
     * @param keys multiple keys to get
     * @return List of ImageMeta linked to key
     */
    public List<ImageMeta> get(List<String> keys) {
        return keys.stream()
                .map(this::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * @param key key
     * @return ImageMeta
     */
    @Nullable
    public ImageMeta get(String key) {
        return metaMapper.load(ImageMeta.class, key);
    }

    /**
     * @param meta image meta to save
     */
    public void put(ImageMeta meta) {
        metaMapper.save(meta.getKey(), meta);
    }

    /**
     * @param key key of ImageMeta to remove
     */
    public void delete(String key) {
        metaMapper.remove(key);
    }
}
