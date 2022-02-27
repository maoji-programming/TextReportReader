package org.maoji.programming.textreportreader.annotation;

import org.maoji.programming.textreportreader.annotation.value.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define the column of table that starting position and ending position
 * during reading the line in .txt file.
 *
 *
 * @author Maoji
 * @version 1.0
 *
 * @see TxtReportDictionary example in TxtReportDictionary
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Pair {
    /**
     * The key word of the pair appears in .txt files.
     * */
    String key();
    /**
     * The capturing length of value following the key word.
     * */
    int valueLength();
    /**
     * The capturing pattern(regular expression) of value following the key word.
     *
     * @see java.util.regex.Pattern
     * */
    String valuePattern();

}
