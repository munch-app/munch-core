package app.munch.model.annotation;

import app.munch.model.validator.MentionTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE_USE;

/**
 * {@link app.munch.model.MentionType#ARTICLE} = {@link app.munch.model.Article}
 * {@link app.munch.model.MentionType#MEDIA} = {@link app.munch.model.ProfileMedia}
 * Date: 28/9/19
 * Time: 9:44 pm
 *
 * @author Fuxing Loh
 * @see app.munch.model.MentionType
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE_USE})
@Constraint(validatedBy = MentionTypeValidator.class)
public @interface ValidMentionType {
    String message() default "mention & mention.type not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
