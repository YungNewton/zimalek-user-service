package com.zmarket.userservice.annotations;

import com.zmarket.userservice.validators.PhoneNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {PhoneNumberValidator.class})
@Retention(RUNTIME)
@Target({ElementType.TYPE})
public @interface ValidPhoneNumber {
    String message() default "valid USA Number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String phone();

    String countryCode();

}
