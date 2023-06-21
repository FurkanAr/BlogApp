package com.demo.Blog.validator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserRoleValidator implements ConstraintValidator<UserRoleSubset, CharSequence> {

    private List<String> acceptedValues;


    @Override
    public void initialize(UserRoleSubset constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        acceptedValues = Stream.of(constraintAnnotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return acceptedValues.contains(value.toString());
    }
}
