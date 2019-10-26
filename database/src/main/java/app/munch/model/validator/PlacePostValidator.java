package app.munch.model.validator;

import app.munch.model.*;
import app.munch.model.annotation.ValidPlacePost;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Date: 28/9/19
 * Time: 9:46 pm
 *
 * @author Fuxing Loh
 */
public class PlacePostValidator implements ConstraintValidator<ValidPlacePost, PlacePost> {

    @Override
    public boolean isValid(PlacePost post, ConstraintValidatorContext context) {
        int count = 0;

        if (post.getContent() != null) {
            count++;
        }
        if (post.getImages() != null && !post.getImages().isEmpty()) {
            count++;
        }

        return count > 0;
    }
}
