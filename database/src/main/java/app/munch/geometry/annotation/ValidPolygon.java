package app.munch.geometry.annotation;

import app.munch.geometry.validator.PolygonValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE_USE;

/**
 * @author Fuxing Loh
 * @since 2019-11-25 at 08:16
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE_USE})
@Constraint(validatedBy = PolygonValidator.class)
public @interface ValidPolygon {
    String message() default "polygon is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
