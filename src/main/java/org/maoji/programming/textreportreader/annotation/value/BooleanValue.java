package org.maoji.programming.textreportreader.annotation.value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicates that the field of model is boolean value.
 * <p>
 * Available field's datatype:
 * <ul>
 *     <li>Boolean</li>
 * </ul>
 * </p>
 * @author Maoji
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BooleanValue {
    /**
     * Set the value to true if getting the keyword.
     * */
    String keywordOfTrue();
    /**
     * Set the value to false if getting the keyword.
     * */
    String keywordOfFalse();
}
