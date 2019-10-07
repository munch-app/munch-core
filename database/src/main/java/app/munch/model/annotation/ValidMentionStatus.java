package app.munch.model.annotation;

import app.munch.model.validator.MentionStatusValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE_USE;

/**
 * {@link app.munch.model.MentionStatus#PUBLIC}: Place is required
 * {@link app.munch.model.MentionStatus#SUGGESTED}: Place is required
 * {@link app.munch.model.MentionStatus#DELETED}: Place is required
 * <p>
 * Date: 28/9/19
 * Time: 9:44 pm
 *
 * @author Fuxing Loh
 * @see app.munch.model.MentionStatus
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE_USE})
@Constraint(validatedBy = MentionStatusValidator.class)
public @interface ValidMentionStatus {

    String message() default "mention & mention.status not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
