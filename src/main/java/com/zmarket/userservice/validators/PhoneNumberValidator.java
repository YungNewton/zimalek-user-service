package com.zmarket.userservice.validators;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.zmarket.userservice.annotations.ValidPhoneNumber;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

@Slf4j
@Component
@Getter
@Setter
public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, Object> {

    private  String phone;

    private String countryCode;

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
        this.phone = constraintAnnotation.phone();
        this.countryCode = constraintAnnotation.countryCode();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {

        Object phoneNumber = new BeanWrapperImpl(value).getPropertyValue(phone);
        Object code = new BeanWrapperImpl(value).getPropertyValue(countryCode);

        if (Objects.isNull(phoneNumber)) {
            return true;
        }

        if (Objects.isNull(code)) {
            return false;
        }


        return validUSANumber(phoneNumber.toString(), code.toString());

    }
    private boolean validUSANumber(String phoneNumber, String code) {

        phoneNumber = phoneNumber.replaceAll(" ", "").replaceAll("-", "").replaceAll("\\.", "");

        try {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

            Phonenumber.PhoneNumber ph = phoneNumberUtil.parse(phoneNumber, Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name());

            return phoneNumberUtil.isValidNumberForRegion(ph, code);

        } catch (NumberParseException exception) {

            log.info("Invalid Phone Number " + exception.getMessage());

            return false;
        }

    }

}