package app.munch.api.migration;

import app.munch.model.Image;
import app.munch.model.Place;
import app.munch.model.PlaceImage;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import dev.fuxing.jpa.TransactionProvider;
import munch.gallery.PlaceImageClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 27/8/19
 * Time: 10:11 PM
 * Project: munch-core
 */
@Singleton
public final class PlaceMigrationManager {

    private final Table table;

    private final TransactionProvider provider;

    private final ImageMigrationManager imageMigrationManager;
    private final PlaceImageClient imageClient;

    @Inject
    PlaceMigrationManager(DynamoDB dynamoDB, TransactionProvider provider, ImageMigrationManager imageMigrationManager, PlaceImageClient imageClient) {
        this.provider = provider;
        this.imageClient = imageClient;
        this.imageMigrationManager = imageMigrationManager;

        this.table = dynamoDB.getTable("munch-core.EntityMigration");
    }

    public List<PlaceImage> getPlaceImages(String id) {
        return provider.reduce(entityManager -> {
            Place place = entityManager.find(Place.class, id);

            List<munch.gallery.PlaceImage> images = imageClient.list(place.getCid(), null, 10);
            return images.stream()
                    .map(placeImage -> mapPlaceImage(entityManager, place, placeImage))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        });
    }

    private PlaceImage mapPlaceImage(EntityManager entityManager, Place place, munch.gallery.PlaceImage galleryImage) {
        String imageId = galleryImage.getImageId();

        String id = "PlaceImage." + imageId;
        Item item = table.getItem("id", id);
        if (item != null) {
            String uid = item.getString("uid");
            return entityManager.find(PlaceImage.class, uid);
        } else {
            Image image = imageMigrationManager.post(entityManager, galleryImage.getSizes());
            if (image == null) return null;

            PlaceImage placeImage = new PlaceImage();
            placeImage.setImage(image);
            placeImage.setProfile(image.getProfile());
            placeImage.setPlace(place);

            entityManager.persist(placeImage);
            Objects.requireNonNull(placeImage.getUid());

            item = new Item();
            item.withString("id", id);
            item.withString("uid", placeImage.getUid());
            table.putItem(item);
            return placeImage;
        }
    }
}
