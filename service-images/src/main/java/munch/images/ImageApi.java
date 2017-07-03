package munch.images;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.restful.server.RestfulServer;

/**
 * Created by: Fuxing
 * Date: 3/7/2017
 * Time: 11:25 PM
 * Project: munch-core
 */
@Singleton
final class ImageApi extends RestfulServer {

    @Inject
    public ImageApi(ImageService image, PutService put) {
        super(image, put);
    }

    @Override
    public void start(int port) {
        super.start(port);
    }
}
