package com.example.product.validation;

import com.example.product.annotation.Unique;
import com.example.product.dto.UniqueIdenfiable;
import com.example.product.service.UniqueValidationService;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UniqueValidator implements ConstraintValidator<Unique, UniqueIdenfiable> {

  private final ApplicationContext context;

  private UniqueValidationService validationService;

  @Override
  public void initialize(Unique constraintAnnotation) {
    Class<? extends UniqueValidationService> serviceClass = constraintAnnotation.service();
    validationService = context.getBean(serviceClass);
  }

  @Override
  public boolean isValid(UniqueIdenfiable value, ConstraintValidatorContext context) {
    List<String> invalidFields = validationService.findInvalidFields(value.getId());
    if (invalidFields == null || invalidFields.isEmpty()) {
      return true;
    }

    context.disableDefaultConstraintViolation();
    invalidFields.parallelStream()
        .forEach(
            f ->
                context
                    .buildConstraintViolationWithTemplate(
                        context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(f)
                    .addConstraintViolation());

    return false;
  }
}
