package app.munch.worker;

import app.munch.database.DatabaseModule;
import app.munch.elastic.ElasticModule;
import app.munch.elastic.ElasticSerializableClient;
import app.munch.image.ImageModule;
import app.munch.migration.PlaceMigration;
import app.munch.model.Place;
import app.munch.model.WorkerTask;
import com.google.inject.AbstractModule;
import dev.fuxing.utils.SleepUtils;
import munch.data.client.PlaceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restful.aws.DynamoModule;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 9/9/19
 * Time: 3:04 pm
 */
@Singleton
public final class PlaceWorker implements WorkerRunner {
    private static final Logger logger = LoggerFactory.getLogger(PlaceWorker.class);

    private final PlaceClient placeClient;
    private final PlaceMigration placeMigration;
    private final ElasticSerializableClient serializableClient;

    @Inject
    PlaceWorker(PlaceClient placeClient, PlaceMigration placeMigration, ElasticSerializableClient serializableClient) {
        this.placeClient = placeClient;
        this.placeMigration = placeMigration;
        this.serializableClient = serializableClient;
    }

    @Override
    public String groupUid() {
        return "01dmadgnrz4p34kf07jwpc2m6r";
    }

    @Override
    public void run(WorkerTask task) throws Exception {
        placeClient.iterator().forEachRemaining(place -> {
            SleepUtils.sleep(100);

            // Mapping
            Place mappedPlace = placeMigration.map(place);
            placeMigration.getPlaceImages(mappedPlace.getId(), 10);

            serializableClient.put(mappedPlace);
            logger.info("Mapped: {}", mappedPlace.getId());
        });
    }

    public static class Module extends AbstractModule {
        @Override
        protected void configure() {
            install(new DatabaseModule());
            install(new ElasticModule());
            install(new ImageModule());
            install(new DynamoModule());
        }
    }

    public static void main(String[] args) {
        WorkerRunner.start(PlaceWorker.class, new Module());
    }
}
