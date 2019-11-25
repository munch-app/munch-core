package app.munch.geometry.validator;

import app.munch.geometry.Coordinate;
import app.munch.geometry.MultiPolygon;
import app.munch.geometry.annotation.ValidMultiPolygon;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * @author Fuxing Loh
 * @since 2019-11-25 at 08:28
 */
public class MultiPolygonValidator implements ConstraintValidator<ValidMultiPolygon, MultiPolygon> {
    @Override
    public boolean isValid(MultiPolygon value, ConstraintValidatorContext context) {
        List<List<List<Coordinate>>> polygons = value.getCoordinates();

        for (List<List<Coordinate>> polygon : polygons) {
            if (!PolygonValidator.isValid(polygon)) {
                return false;
            }
        }

        return true;
    }
}
