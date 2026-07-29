package su35.setec.authservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        boolean length = value.length() >= 8;
        boolean upper = value.chars().anyMatch(Character::isUpperCase);
        boolean lower = value.chars().anyMatch(Character::isLowerCase);
        boolean digit = value.chars().anyMatch(Character::isDigit);
        boolean special = value.chars().anyMatch(ch -> !Character.isLetterOrDigit(ch));

        return length && upper && lower && digit && special;
    }
}
