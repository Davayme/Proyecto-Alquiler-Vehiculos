package com.car.rental.demo.Decorator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<ValidPhoneNumber, String>{

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
    }
    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null || phone.isEmpty()) 
            return false;
        if (!isIdNumberLengthValid(phone)) 
            return false;
        // Solo números
        if(!isNumeric(phone))
            return false;
        return true;
    }

    public static boolean isNumeric(String phone) {
        try {
            Long.parseLong(phone);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isIdNumberLengthValid(String phone) {
        return phone.length() == 10;
    }
    
}
