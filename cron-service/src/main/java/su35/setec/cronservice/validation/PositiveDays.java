package su35.setec.cronservice.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = PositiveDaysValidator.class)
public @interface PositiveDays {

    String message() default "Value must be greater than zero";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
