package com.example.product.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.example.product.service.UniqueValidationService;
import com.example.product.validation.UniqueValidator;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = {UniqueValidator.class})
@Target({TYPE})
@Retention(RUNTIME)
public @interface Unique {

  //  String message() default "{javax.validation.constraints.NotNull.message}";
  String message() default "{javax.validation.constraints.NotUnique.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  Class<? extends UniqueValidationService> service();
}
