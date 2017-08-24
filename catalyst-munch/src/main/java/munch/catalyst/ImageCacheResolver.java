package munch.catalyst;

import catalyst.data.DataClient;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import corpus.data.CorpusData;
import corpus.utils.FieldUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;

/**
 * Created by: Fuxing
 * Date: 6/8/2017
 * Time: 6:20 PM
 * Project: munch-corpus
 */
@Singleton
public class ImageCacheResolver {

    private final DataClient dataClient;

    @Inject
    public ImageCacheResolver(DataClient dataClient) {
        this.dataClient = dataClient;
    }

    @Nullable
    public CorpusData.Field resolve(String url) {
        if (StringUtils.isBlank(url)) return null;

        String hash = DigestUtils.sha256Hex(url);
        CorpusData corpusData = dataClient.getCorpusData("Sg.Munch.ImageCache", hash);
        if (corpusData == null) return null;

        // Metadata is the images node
        return FieldUtils.get(corpusData, "Sg.Munch.ImageCache.imageKey")
                .orElse(null);
    }

}
