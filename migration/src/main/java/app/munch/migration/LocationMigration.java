package app.munch.migration;

import app.munch.geometry.Coordinate;
import app.munch.geometry.Geometry;
import app.munch.geometry.Polygon;
import app.munch.model.*;
import app.munch.utils.spatial.LatLng;
import dev.fuxing.jpa.TransactionProvider;
import munch.data.location.Area;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fuxing Loh
 * @since 2019-11-23 at 10:16
 */
@Singleton
public final class LocationMigration {

    private final TransactionProvider provider;

    private final ImageMigration imageMigrationManager;
    private final EntityMigrationTable entityMigrationTable;

    @Inject
    LocationMigration(TransactionProvider provider, ImageMigration imageMigrationManager, EntityMigrationTable entityMigrationTable) {
        this.provider = provider;
        this.imageMigrationManager = imageMigrationManager;
        this.entityMigrationTable = entityMigrationTable;
    }

    public LocationRevision persist(Area area) {
        return provider.reduce(false, entityManager -> {
            LocationRevision revision = map(entityManager, area);
            entityManager.persist(revision);
            return revision;
        });
    }

    public LocationRevision map(EntityManager entityManager, Area area) {
        Location location = new Location();

        LocationRevision revision = new LocationRevision();
        revision.setName(StringUtils.substring(area.getName(), 0, 100));
        revision.setDescription(StringUtils.substring(area.getDescription(), 0, 250));

        revision.setPostcode(area.getLocation().getPostcode());
        revision.setAddress(area.getLocation().getAddress());
        revision.setLatLng(area.getLocation().getLatLng());

        revision.setType(LocationType.OTHERS);
        revision.setSynonyms(area.getNames());

        revision.setCreatedBy(Profile.findByUid(entityManager, Profile.COMPAT_ID));
        revision.setLocation(location);

        location.setImage(mapImage(entityManager, area));
        revision.setGeometry(mapGeometry(area));


        if (revision.getAddress() == null) {
            if (revision.getPostcode() != null) {
                revision.setAddress(revision.getName() + ", Singapore " + revision.getPostcode());
            } else {
                revision.setAddress(revision.getName() + ", Singapore");
            }
        }
        return revision;
    }

    public Geometry mapGeometry(Area area) {
        Polygon polygon = new Polygon();
        List<Coordinate> coordinates = new ArrayList<>();

        area.getLocation().getPolygon().getPoints().forEach(s -> {
            LatLng latLng = LatLng.parse(s);
            coordinates.add(new Coordinate(latLng.getLng(), latLng.getLat()));
        });

        polygon.setCoordinates(List.of(coordinates));
        return polygon;
    }

    @Nullable
    public Image mapImage(EntityManager entityManager, Area area) {
        @NotNull List<munch.file.Image> images = area.getImages();
        if (images.isEmpty()) {
            return null;
        }
        return mapImage(entityManager, images.get(0));
    }

    public Image mapImage(EntityManager entityManager, munch.file.Image fileImage) {
        String imageId = fileImage.getImageId();

        String uid = entityMigrationTable.getUID("LocationImage", imageId);
        if (uid != null) {
            return entityManager.find(Image.class, uid);
        }

        Image image = imageMigrationManager.post(entityManager, imageId, fileImage.getSizes());
        if (image == null) return null;

        entityMigrationTable.putUID("LocationImage", imageId, image.getUid());
        return image;
    }
}
