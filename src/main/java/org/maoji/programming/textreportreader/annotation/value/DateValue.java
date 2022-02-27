package org.maoji.programming.textreportreader.annotation.value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the field of model is date value.
 *
 * <p>
 * Available field's datatype:
 * <ul>
 *     <li>Date</li>
 * </ul>
 * </p>
 * @author Maoji
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DateValue {
    /**
     * Convert the value to date with given format.
     * */
    String format();
}
