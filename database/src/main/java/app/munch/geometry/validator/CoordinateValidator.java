package app.munch.geometry.validator;

import app.munch.geometry.Coordinate;
import app.munch.geometry.annotation.ValidCoordinate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Fuxing Loh
 * @since 2019-11-25 at 09:10
 */
public class CoordinateValidator implements ConstraintValidator<ValidCoordinate, Coordinate> {

    @Override
    public boolean isValid(Coordinate value, ConstraintValidatorContext context) {
        // latitude: -90 and +90
        if (value.getLatitude() < -90) return false;
        if (value.getLatitude() > 90) return false;

        // longitude: -180 and +180
        if (value.getLongitude() < -180) return false;
        if (value.getLongitude() > 180) return false;
        return true;
    }
}
