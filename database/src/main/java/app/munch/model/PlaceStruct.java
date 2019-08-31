package app.munch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.fuxing.err.ValidationException;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.postgres.PojoUserType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Place Struct is a non-persistence, non-validated (except for Default.class).
 * It is to be used for entity to store place information without veracity and consistency.
 * It comes with a basic builder to ensure it's always valid no matter how it's filled.
 * <p>
 * Created by: Fuxing
 * Date: 30/8/19
 * Time: 2:08 PM
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class PlaceStruct extends PlaceModel {
    private static final Logger logger = LoggerFactory.getLogger(PlaceStruct.class);

    public static final class UserType extends PojoUserType<PlaceStruct> {
        public UserType() {
            super(PlaceStruct.class);
        }
    }

    @Singleton
    public static final class BuilderFactory {
        private final Set<Tag> tags;

        @Inject
        BuilderFactory(TransactionProvider provider) {
            this.tags = provider.reduce(entityManager -> {
                Set<Tag> tags = new HashSet<>();
                entityManager.createQuery("FROM Tag", Tag.class)
                        .getResultList()
                        .forEach(tag -> {
                            tag.setUpdatedAt(null);
                            tag.setCreatedAt(null);
                            tags.add(tag);
                        });
                return tags;
            });
        }

        @Nullable
        private Tag findTag(String text) {
            return tags.stream()
                    .filter(tag -> tag.getName().equalsIgnoreCase(text))
                    .findFirst()
                    .orElse(null);
        }

        public Builder newBuilder() {
            return new Builder(this);
        }
    }

    public static final class Builder {
        private final BuilderFactory factory;
        private final PlaceStruct place;

        private Builder(BuilderFactory factory) {
            this.factory = factory;
            this.place = new PlaceStruct();
        }

        public Builder name(String name) {
            name = StringUtils.substring(name, 0, 100);
            name = StringUtils.trimToNull(name);
            this.place.setName(name);
            return this;
        }

        public Builder phone(String phone) {
            phone = StringUtils.substring(phone, 0, 100);
            phone = StringUtils.trimToNull(phone);
            this.place.setPhone(phone);
            return this;
        }

        public Builder website(String website) {
            website = StringUtils.substring(website, 0, 1000);
            website = StringUtils.trimToNull(website);
            this.place.setWebsite(website);
            return this;
        }

        public Builder description(String description) {
            description = StringUtils.substring(description, 0, 250);
            description = StringUtils.trimToNull(description);
            this.place.setDescription(description);
            return this;
        }


        public Builder address(String address) {
            address = StringUtils.substring(address, 0, 255);
            address = StringUtils.trimToNull(address);
            if (address == null) {
                return this;
            }

            if (this.place.getLocation() == null) {
                this.place.setLocation(new Location());
            }
            this.place.getLocation().setAddress(address);
            return this;
        }

        @SuppressWarnings("DuplicatedCode")
        public Builder unitNumber(String unitNumber) {
            unitNumber = StringUtils.substring(unitNumber, 0, 100);
            unitNumber = StringUtils.trimToNull(unitNumber);
            if (unitNumber == null) {
                return this;
            }

            if (this.place.getLocation() == null) {
                this.place.setLocation(new Location());
            }
            this.place.getLocation().setUnitNumber(unitNumber);
            return this;
        }

        @SuppressWarnings("DuplicatedCode")
        public Builder postcode(String postcode) {
            postcode = StringUtils.substring(postcode, 0, 100);
            postcode = StringUtils.trimToNull(postcode);
            if (postcode == null) {
                return this;
            }

            if (this.place.getLocation() == null) {
                this.place.setLocation(new Location());
            }
            this.place.getLocation().setPostcode(postcode);
            return this;
        }

        public Builder latLng(String latLng) {
            if (latLng == null) {
                return this;
            }

            if (this.place.getLocation() == null) {
                this.place.setLocation(new Location());
            }
            this.place.getLocation().setLatLng(latLng);
            return this;
        }

        public Builder pricePerPax(BigDecimal perPax) {
            if (perPax == null) return this;

            if (this.place.getPrice() == null) {
                this.place.setPrice(new Price());
            }
            this.place.getPrice().setPerPax(perPax);
            return this;
        }

        public Builder synonyms(String... names) {
            Set<String> synonyms = Arrays.stream(names)
                    .map(s -> StringUtils.substring(s, 0, 100))
                    .filter(StringUtils::isNotBlank)
                    .limit(4)
                    .collect(Collectors.toSet());

            if (synonyms.isEmpty()) return this;

            this.place.setSynonyms(synonyms);
            return this;
        }

        public Builder tags(String... tags) {
            Set<Tag> set = Arrays.stream(tags)
                    .map(factory::findTag)
                    .filter(Objects::nonNull)
                    .limit(12)
                    .collect(Collectors.toSet());

            if (set.isEmpty()) return this;

            this.place.setTags(set);
            return this;
        }

        public Builder tags(Tag... tags) {
            Set<Tag> set = Arrays.stream(tags)
                    .limit(12)
                    .collect(Collectors.toSet());

            if (set.isEmpty()) return this;

            this.place.setTags(set);
            return this;
        }

        public Builder hours(Hour... hours) {
            Set<Hour> set = Arrays.stream(hours)
                    .limit(24)
                    .collect(Collectors.toSet());

            if (set.isEmpty()) return this;

            this.place.setHours(set);
            return this;
        }

        public Builder hours(Triple<String, LocalTime, LocalTime>... hours) {
            Set<Hour> set = Arrays.stream(hours)
                    .map(triple -> {
                        if (triple.getLeft() == null) return null;
                        if (triple.getMiddle() == null) return null;
                        if (triple.getRight() == null) return null;

                        Hour hour = new Hour();
                        hour.setOpen(triple.getMiddle().getHour() * 60 + triple.getMiddle().getMinute());
                        hour.setClose(triple.getRight().getHour() * 60 + triple.getRight().getMinute());
                        if (hour.getClose() == 1439) {
                            hour.setClose(1440);
                        }

                        switch (triple.getLeft().toLowerCase()) {
                            default:
                                return null;

                            case "mo":
                                hour.setDay(Day.MON);
                                return hour;

                            case "tu":
                                hour.setDay(Day.TUE);
                                return hour;

                            case "we":
                                hour.setDay(Day.WED);
                                return hour;

                            case "th":
                                hour.setDay(Day.THU);
                                return hour;

                            case "fr":
                                hour.setDay(Day.FRI);
                                return hour;

                            case "sat":
                                hour.setDay(Day.SAT);
                                return hour;

                            case "su":
                                hour.setDay(Day.SUN);
                                return hour;
                        }
                    })
                    .filter(Objects::nonNull)
                    .limit(24)
                    .collect(Collectors.toSet());

            this.place.setHours(set);
            return this;
        }

        public Builder statusOpen() {
            this.place.setStatus(new Status.OpenStatus());
            return this;
        }

        public Builder statusClosed(Date date) {
            Status.PermanentlyClosedStatus status = new Status.PermanentlyClosedStatus();
            status.setAt(date);
            this.place.setStatus(status);
            return this;
        }

        public PlaceStruct build() {
            Set<ConstraintViolation<PlaceStruct>> violations = ValidationException.validator.validate(place);
            if (violations.isEmpty()) {
                return place;
            }

            String reasons = violations.stream()
                    .map(violation -> {
                        Path path = violation.getPropertyPath();
                        String message = violation.getMessage();
                        return path.toString() + ": " + message;
                    })
                    .collect(Collectors.joining("\n"));

            logger.trace("PlaceStruct invalid paths: {}\n{}", violations.size(), reasons);
            logger.trace("Attempting fix");
            return fix(violations, place);
        }

        private PlaceStruct fix(Set<ConstraintViolation<PlaceStruct>> violations, PlaceStruct place) {
            for (ConstraintViolation<PlaceStruct> violation : violations) {
                String path = violation.getPropertyPath().toString();

                if (path.equals("website")) {
                    place.setWebsite(null);
                } else if (path.equals("location.latLng")) {
                    place.getLocation().setLatLng(null);
                } else if (path.startsWith("price.")) {
                    place.setPrice(null);
                } else if (path.startsWith("status.")) {
                    place.setStatus(null);
                } else if (path.startsWith("hours")) {
                    place.setHours(null);
                } else if (path.startsWith("tags")) {
                    place.setTags(null);
                } else {
                    logger.trace("Unable to fix Path: {}", path);
                }
            }

            return place;
        }
    }
}
