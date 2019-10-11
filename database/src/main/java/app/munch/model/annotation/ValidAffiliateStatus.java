package app.munch.model.annotation;

import app.munch.model.validator.AffiliateStatusValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE_USE;

/**
 * @author Fuxing Loh
 * @since 2019-10-11 at 11:44 am
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE_USE})
@Constraint(validatedBy = AffiliateStatusValidator.class)
public @interface ValidAffiliateStatus {
    String message() default "affiliate or affiliate.status is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
