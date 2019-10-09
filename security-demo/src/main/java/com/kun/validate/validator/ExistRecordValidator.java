package com.kun.validate.validator;

import com.kun.validate.annotation.ExistRecord;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistRecordValidator implements ConstraintValidator<ExistRecord, Object> {

    @Override
    public void initialize(ExistRecord constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        System.out.println(value);
        return true;
    }
}
