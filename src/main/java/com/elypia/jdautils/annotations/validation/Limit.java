package com.elypia.jdautils.annotations.validation;

import java.lang.annotation.*;

/**
 * Limit the value that a number can be, by default the limits are
 * what a Java {@link Long} can handle.
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Limit {
    double min() default Double.MIN_VALUE;
    double max() default Double.MAX_VALUE;
}
