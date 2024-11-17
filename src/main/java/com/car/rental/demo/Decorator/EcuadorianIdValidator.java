package com.car.rental.demo.Decorator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EcuadorianIdValidator implements ConstraintValidator<ValidEcuadorianId, String> {

    @Override
    public void initialize(ValidEcuadorianId constraintAnnotation) {
    }

    @Override
    public boolean isValid(String idNumber, ConstraintValidatorContext context) {
        if (idNumber == null || idNumber.isEmpty()) {
            return false;
        }
        if (idNumber.length() == 10) return isValidEcuadorianId(idNumber);
        else if (idNumber.length() == 13) return isValidEcuadorianId(idNumber);
        return false;
        
    }

    public static boolean isValidEcuadorianId(String idNumber) {
        boolean response  = false;
        if (!isIdNumberLengthValid(idNumber)) 
            return response;
        // Solo números
        if(!isNumeric(idNumber))
            return response;
        
        if(!isValidEcuadorianIdRange(idNumber))
            return response;

        if(!isThirdDigitValid(idNumber))
            return response;  
       
        if (!checkIdNumberValidity(idNumber))
            return response;
        // Digito verificador
        
        return  true;
    }


    public static boolean isIdNumberLengthValid(String idNumber) {
        return idNumber.length() == 10 || idNumber.length() == 13;
    }

    public static boolean isNumeric(String idNumber) {
        try {
            Long.parseLong(idNumber);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidEcuadorianIdRange(String idNumber) {
        int value = Integer.parseInt(idNumber.substring(0, 2));
        if (value < 1 || value > 24) 
            return false;
        return true;
    }

    public static boolean isThirdDigitValid(String idNumber) {
        int thirdDigit = Integer.parseInt(idNumber.substring(2, 3));
        if (thirdDigit > 5) 
            return false;    
        return true;
    }

    public static boolean checkIdNumberValidity(String idNumber) {
        int[] coefficientArray = {2, 1, 2, 1, 2, 1, 2, 1, 2};
        int sum = 0;
        int verificationDigit = Integer.parseInt(idNumber.substring(9, 10));
        for (int i = 0; i < 9; i++) {
            int value_ = Integer.parseInt(idNumber.substring(i, i + 1));
            int product = value_ * coefficientArray[i];
            if (product >= 10) {
                product -= 9;
            }
            sum += product;
        }
        int remainder = sum % 10 == 0 ? 0 : 10 - (sum % 10);
        System.out.println(remainder);
        if (remainder != verificationDigit) {
            return false;
        }
        return true;
    }
}