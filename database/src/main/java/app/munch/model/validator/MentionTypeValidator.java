package app.munch.model.validator;

import app.munch.model.Mention;
import app.munch.model.MentionType;
import app.munch.model.annotation.ValidMentionType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Date: 28/9/19
 * Time: 9:46 pm
 *
 * @author Fuxing Loh
 */
public class MentionTypeValidator implements ConstraintValidator<ValidMentionType, Mention> {

    @Override
    public boolean isValid(Mention mention, ConstraintValidatorContext context) {
        MentionType type = mention.getType();
        if (type == null) return false;

        int count = 0;
        if (mention.getArticle() != null) count++;
        if (mention.getMedia() != null) count++;

        switch (type) {
            case MEDIA:
                return mention.getMedia() != null && count == 1;

            case ARTICLE:
                return mention.getArticle() != null && count == 1;
        }
        return false;
    }

}
