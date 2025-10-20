package it.csi.risca.riscaboweb.util.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ValidatedBigDecimal {
    double minValue() default Double.NEGATIVE_INFINITY;
    double maxValue() default Double.POSITIVE_INFINITY;
}
