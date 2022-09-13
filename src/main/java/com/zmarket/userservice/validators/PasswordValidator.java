package com.zmarket.userservice.validators;

import com.zmarket.userservice.annotations.ValidPassword;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PasswordValidator implements ConstraintValidator<ValidPassword, Object> {

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        String password = value.toString();


        return containsDigit(password) && containsLowerCase(password) && containsUpperCase(password) && containsLengthBetween(password);

    }

    private static boolean containsDigit(String password) {
        String regex = "(.*\\d.*)";

        Pattern p = Pattern.compile(regex);

        if (password == null) {
            return false;
        }

        Matcher m = p.matcher(password);

        return m.matches();
    }

    private static boolean containsLowerCase(String password) {
        String regex = "(.*[a-z].*)";

        Pattern p = Pattern.compile(regex);

        if (password == null) {
            return false;
        }

        Matcher m = p.matcher(password);

        return m.matches();
    }

    private static boolean containsUpperCase(String password) {
        String regex = "(.*[A-Z].*)";

        Pattern p = Pattern.compile(regex);

        if (password == null) {
            return false;
        }

        Matcher m = p.matcher(password);

        return m.matches();
    }

    private static boolean containsLengthBetween(String password) {
        String regex = ".{8,20}";

        Pattern p = Pattern.compile(regex);

        if (password == null) {
            return false;
        }

        Matcher m = p.matcher(password);

        return m.matches();
    }

}