package munch.api.clients;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import munch.api.struct.Image;
import munch.restful.client.RestfulClient;

import javax.inject.Named;
import java.util.Collection;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 22/4/2017
 * Time: 9:47 PM
 * Project: munch-core
 */
@Singleton
public class ImageClient extends RestfulClient {

    @Inject
    public ImageClient(@Named("services") Config config) {
        super(config.getString("images.url"));
    }

    /**
     * @param keys collection of keys
     * @return list of Image matching the keys
     */
    public List<Image> batch(Collection<String> keys) {
        return doPost("/images/batch/get")
                .body(keys)
                .hasMetaCodes(200)
                .asDataList(Image.class);
    }
}
