package app.munch.migration;

import app.munch.model.Place;
import app.munch.model.PlaceImage;
import dev.fuxing.jpa.TransactionProvider;
import munch.gallery.PlaceImageClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 27/8/19
 * Time: 10:11 PM
 * Project: munch-core
 */
@Singleton
public final class PlaceMigration {

    private final TransactionProvider provider;

    private final PlaceImageClient imageClient;
    private final PlaceEntityMapper placeEntityManager;

    @Inject
    PlaceMigration(TransactionProvider provider, PlaceImageClient imageClient, PlaceEntityMapper placeEntityManager) {
        this.provider = provider;
        this.imageClient = imageClient;
        this.placeEntityManager = placeEntityManager;
    }

    public Place map(munch.data.place.Place deprecatedPlace) {
        String cid = deprecatedPlace.getPlaceId();

        return provider.reduce(entityManager -> {
            app.munch.model.Place place = Optional.of(entityManager.createQuery("FROM Place " +
                    "WHERE cid = :cid", Place.class)
                    .setParameter("cid", cid)
                    .getResultList())
                    .filter(places -> !places.isEmpty())
                    .map(places -> places.get(0))
                    .orElseGet(() -> {
                        Place newPlace = new Place();
                        newPlace.setCid(cid);
                        newPlace.setCreatedAt(new Timestamp(deprecatedPlace.getCreatedMillis()));
                        return newPlace;
                    });

            if (place.getId() != null) {
                // Check whether it's already migrated
                List list = entityManager.createQuery("FROM PlaceRevision WHERE id = :id ")
                        .setParameter("id", place.getId())
                        .setMaxResults(1)
                        .getResultList();

                if (!list.isEmpty()) {
                    return place;
                }
            }

            // Whenever queried it's automatically updated
            placeEntityManager.map(entityManager, place, deprecatedPlace);
            entityManager.persist(place);
            return place;
        });
    }

    public List<PlaceImage> getPlaceImages(String id) {
        return getPlaceImages(id, 10);
    }

    public List<PlaceImage> getPlaceImages(String id, int size) {
        return provider.reduce(entityManager -> {
            Place place = entityManager.find(Place.class, id);

            List<munch.gallery.PlaceImage> images = imageClient.list(place.getCid(), null, size);
            return images.stream()
                    .map(placeImage -> placeEntityManager.mapPlaceImage(entityManager, place, placeImage))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        });
    }
}
