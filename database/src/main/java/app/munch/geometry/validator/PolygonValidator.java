package app.munch.geometry.validator;

import app.munch.geometry.Coordinate;
import app.munch.geometry.Polygon;
import app.munch.geometry.annotation.ValidPolygon;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * @author Fuxing Loh
 * @since 2019-11-25 at 08:28
 */
public class PolygonValidator implements ConstraintValidator<ValidPolygon, Polygon> {
    @Override
    public boolean isValid(Polygon value, ConstraintValidatorContext context) {
        return isValid(value.getCoordinates());
    }

    /**
     * Note: Inner polygon is not validated
     *
     * @param polygon to valid
     * @return validity of polygon
     */
    public static boolean isValid(List<List<Coordinate>> polygon) {
        for (List<Coordinate> coordinates : polygon) {
            if (coordinates.size() < 4) {
                return false;
            }

            Coordinate first = coordinates.get(0);
            Coordinate last = coordinates.get(coordinates.size() - 1);
            if (!first.equals(last)) {
                 return false;
            }
        }
        return true;
    }
}
