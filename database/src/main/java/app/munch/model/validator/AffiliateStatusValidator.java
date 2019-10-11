package app.munch.model.validator;

import app.munch.model.Affiliate;
import app.munch.model.AffiliateStatus;
import app.munch.model.annotation.ValidAffiliateStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Fuxing Loh
 * @since 2019-10-11 at 11:46 am
 */
public class AffiliateStatusValidator implements ConstraintValidator<ValidAffiliateStatus, Affiliate> {

    @Override
    public boolean isValid(Affiliate value, ConstraintValidatorContext context) {
        AffiliateStatus status = value.getStatus();
        if (status == null) return false;

        switch (status) {
            case LINKED:
                if (value.getPlace() == null) return false;
                if (value.getLinked() == null) return false;
                if (value.getEditedBy() == null) return false;
                return true;

            case PENDING:
            case DELETED_MUNCH:
            case DELETED_SOURCE:
                if (value.getLinked() != null) return false;
                return true;

            case DROPPED:
            case REAPPEAR:
                if (value.getPlace() == null) return false;
                if (value.getEditedBy() == null) return false;
                return true;

            default:
                return false;
        }
    }
}
