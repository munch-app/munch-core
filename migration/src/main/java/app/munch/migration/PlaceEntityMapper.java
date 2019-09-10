package app.munch.migration;

import app.munch.model.*;
import munch.data.location.Location;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 14/8/19
 * Time: 2:05 pm
 */
@Singleton
public final class PlaceEntityMapper {

    private final ImageMigration imageMigrationManager;
    private final EntityMigrationTable entityMigrationTable;

    @Inject
    PlaceEntityMapper(ImageMigration imageMigrationManager, EntityMigrationTable entityMigrationTable) {
        this.imageMigrationManager = imageMigrationManager;
        this.entityMigrationTable = entityMigrationTable;
    }

    public void map(EntityManager entityManager, Place place, munch.data.place.Place deprecatedPlace) {
        munch.data.place.Place.@NotNull @Valid Taste taste = deprecatedPlace.getTaste();
        place.setImportant(taste.getImportance());
        place.setCreatedBy(entityManager.find(Profile.class, Profile.COMPAT_ID));

        place.setName(StringUtils.substring(deprecatedPlace.getName(), 0, 100));
        place.setPhone(deprecatedPlace.getPhone());
        place.setWebsite(deprecatedPlace.getWebsite());
        place.setDescription(StringUtils.substring(deprecatedPlace.getDescription(), 0, 250));

        if (deprecatedPlace.getPrice() != null) {
            Place.Price price = new PlaceModel.Price();
            price.setPerPax(new BigDecimal(deprecatedPlace.getPrice().getPerPax()));
            place.setPrice(price);
        }

        Location deprecatedLocation = deprecatedPlace.getLocation();
        PlaceModel.Location location = new PlaceModel.Location();
        location.setLatLng(deprecatedLocation.getLatLng());
        location.setAddress(deprecatedLocation.getAddress());
        location.setPostcode(deprecatedLocation.getPostcode());
        location.setUnitNumber(deprecatedLocation.getUnitNumber());
        place.setLocation(location);

        switch (deprecatedPlace.getStatus().getType()) {
            case open:
                place.setStatus(new Status.OpenStatus());
                break;

            case closed:
                Status.PermanentlyClosedStatus closedStatus = new Status.PermanentlyClosedStatus();
                closedStatus.setAt(new Date(deprecatedPlace.getUpdatedMillis()));
                place.setStatus(closedStatus);
                break;

            case moved:
            case deleted:
            case renamed:
            case redirected:
            case renovation:
                Status.DeletedStatus deletedStatus = new Status.DeletedStatus();
                deletedStatus.setAt(new Date(deprecatedPlace.getUpdatedMillis()));
                place.setStatus(deletedStatus);
        }

        Set<String> names = deprecatedPlace.getNames().stream()
                .map(s -> StringUtils.substring(s, 0, 100))
                .filter(Objects::nonNull)
                .limit(4)
                .collect(Collectors.toSet());
        place.setSynonyms(names);

        Set<Hour> hours = deprecatedPlace.getHours().stream()
                .map(this::mapHour)
                .collect(Collectors.toSet());
        if (hours.size() < 24) {
            place.setHours(hours);
        } else {
            place.setHours(Set.of());
        }

        Set<Tag> tags = deprecatedPlace.getTags()
                .stream()
                .map(deprecatedTag -> {
                    Tag tag = mapTag(deprecatedTag);

                    List<Tag> list = entityManager.createQuery("FROM Tag " +
                            "WHERE type = :tagType AND name = :name", Tag.class)
                            .setParameter("tagType", tag.getType())
                            .setParameter("name", tag.getName())
                            .getResultList();

                    if (!list.isEmpty()) {
                        return list.get(0);
                    }

                    entityManager.persist(tag);
                    return tag;
                }).limit(12).collect(Collectors.toSet());
        place.setTags(tags);


        place.setImage(mapImage(entityManager, deprecatedPlace.getImages()));
    }

    public PlaceImage mapPlaceImage(EntityManager entityManager, Place place, munch.gallery.PlaceImage galleryImage) {
        String imageId = galleryImage.getImageId();

        String uid = entityMigrationTable.getUID("PlaceImage", imageId);
        if (uid != null) {
            return entityManager.find(PlaceImage.class, uid);
        }

        Image image = imageMigrationManager.post(entityManager, galleryImage.getImageId(), galleryImage.getSizes());
        if (image == null) return null;

        PlaceImage placeImage = new PlaceImage();
        placeImage.setImage(image);
        placeImage.setProfile(image.getProfile());
        placeImage.setPlace(place);

        entityManager.persist(placeImage);
        Objects.requireNonNull(placeImage.getUid());

        entityMigrationTable.putUID("PlaceImage", imageId, placeImage.getUid());
        return placeImage;
    }


    private Hour mapHour(munch.data.Hour deprecatedHour) {
        Hour hour = new Hour();
        hour.setDay(Day.fromValue(deprecatedHour.getDay().name().toUpperCase()));

        String[] opens = deprecatedHour.getOpen().split(":");
        hour.setOpen(Integer.parseInt(opens[0]) * 60 + Integer.parseInt(opens[1]));

        String[] closes = deprecatedHour.getClose().split(":");
        int time = Integer.parseInt(closes[0]) * 60 + Integer.parseInt(closes[1]);
        if (time == 1439) time = 1440;
        hour.setClose(time);
        return hour;
    }

    private Tag mapTag(munch.data.place.Place.Tag deprecatedTag) {
        Tag tag = new Tag();

        switch (deprecatedTag.getType()) {
            case Food:
                tag.setType(TagType.FOOD);
                tag.setName(deprecatedTag.getName());
                return tag;
            case Cuisine:
                tag.setType(TagType.CUISINE);
                tag.setName(deprecatedTag.getName());
                return tag;

            case Timing:
            case Amenities:
            case Requirement:
            case Establishment:
            default:
                tag.setType(TagType.AMENITIES);
                tag.setName(deprecatedTag.getName());
                return tag;
        }
    }

    private Image mapImage(EntityManager entityManager, List<munch.file.Image> images) {
        if (images.isEmpty()) return null;

        munch.file.Image fileImage = images.get(0);
        List<munch.file.Image.Size> sizes = fileImage.getSizes();

        String url = sizes.stream().max(Comparator.comparing(munch.file.Image.Size::getHeight))
                .map(munch.file.Image.Size::getUrl)
                .orElse(null);

        if (url == null) return null;

        try {
            return imageMigrationManager.post(entityManager, fileImage.getImageId(), url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
