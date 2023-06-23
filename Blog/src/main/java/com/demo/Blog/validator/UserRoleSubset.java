package com.demo.Blog.validator;


import com.demo.Blog.model.enums.UserRole;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = UserRoleValidator.class)
public @interface UserRoleSubset {

    Class<? extends Enum<?>> enumClass();
    String message() default "Must be any of enum ADMIN or USER";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
