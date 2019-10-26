package app.munch.model.annotation;

import app.munch.model.validator.PlacePostValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE_USE;

/**
 * @author Fuxing Loh
 * @since 2019-10-26 at 17:28
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE_USE})
@Constraint(validatedBy = PlacePostValidator.class)
public @interface ValidPlacePost {
    String message() default "content or images is required";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
