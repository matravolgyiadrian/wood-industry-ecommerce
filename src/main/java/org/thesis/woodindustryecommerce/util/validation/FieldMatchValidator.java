package org.thesis.woodindustryecommerce.util.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String[] fields;
    private String message;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        fields = constraintAnnotation.fields();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        final BeanWrapperImpl beanWrapper = new BeanWrapperImpl(obj);

        boolean isValid = fields.length == 0 || Arrays.stream(fields)
                .map(beanWrapper::getPropertyValue).filter(Objects::nonNull)
                .allMatch(field -> field.equals(beanWrapper.getPropertyValue(fields[0])));

        if (!isValid) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(fields[0])
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        return isValid;
    }
}
