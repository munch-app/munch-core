package app.munch.model.validator;

import app.munch.model.Mention;
import app.munch.model.MentionStatus;
import app.munch.model.annotation.ValidMentionType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Date: 28/9/19
 * Time: 9:46 pm
 *
 * @author Fuxing Loh
 */
public class MentionStatusValidator implements ConstraintValidator<ValidMentionType, Mention> {

    @Override
    public boolean isValid(Mention mention, ConstraintValidatorContext context) {
        MentionStatus status = mention.getStatus();
        if (status == null) return false;

        switch (status) {
            case PENDING:
                return mention.getPlace() == null;

            case DELETED:
            case LINKED:
            case LINK_SUGGEST:
                return mention.getPlace() != null;
        }

        return false;
    }

}
