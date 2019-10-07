package app.munch.model.validator;

import app.munch.model.Mention;
import app.munch.model.MentionStatus;
import app.munch.model.annotation.ValidMentionStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Date: 28/9/19
 * Time: 9:46 pm
 *
 * @author Fuxing Loh
 */
public class MentionStatusValidator implements ConstraintValidator<ValidMentionStatus, Mention> {

    @Override
    public boolean isValid(Mention mention, ConstraintValidatorContext context) {
        MentionStatus status = mention.getStatus();
        if (status == null) return false;

        switch (status) {
            case DELETED:
            case PUBLIC:
            case SUGGESTED:
                return mention.getPlace() != null;
        }

        return false;
    }

}
