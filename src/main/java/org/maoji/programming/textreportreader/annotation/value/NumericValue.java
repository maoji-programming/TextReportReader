package org.maoji.programming.textreportreader.annotation.value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.RoundingMode;

/**
 * Indicates that the field of model is numeric value.
 *
 * <p>
 * Available field's datatype:
 * <ul>
 *     <li>Integer</li>
 *     <li>Long</li>
 *     <li>Float</li>
 *     <li>Double</li>
 *     <li>BigDecimal</li>
 * </ul>
 * </p>
 * @author Maoji
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NumericValue {
    /**
     * Scale of the number. The default value is 0
     *
     */
    int scale() default 0;
    /**
     * Precision of the number. The default value is -1
     */
    int precision() default -1;

    /**
     * Mode of rounding. The default value is HALF_UP
     *
     * @see java.math.RoundingMode
     */
    RoundingMode roundingMode() default RoundingMode.HALF_UP;
}
