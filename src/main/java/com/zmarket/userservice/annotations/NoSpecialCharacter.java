package com.zmarket.userservice.annotations;

import com.zmarket.userservice.validators.NoSpecialCharacterValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {NoSpecialCharacterValidator.class})
@Retention(RUNTIME)
@Target({ElementType.FIELD})
public @interface NoSpecialCharacter {
    String message() default "must not contain special character";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
