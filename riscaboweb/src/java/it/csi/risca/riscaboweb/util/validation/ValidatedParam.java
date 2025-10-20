package it.csi.risca.riscaboweb.util.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ValidatedParam {
    int minValue() default Integer.MIN_VALUE;
    int maxValue() default Integer.MAX_VALUE;
    String name() default "";
}
