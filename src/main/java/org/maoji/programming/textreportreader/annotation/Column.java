package org.maoji.programming.textreportreader.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define the column of table with starting position and ending position
 * during reading the line in .txt file.
 *
 * @author Maoji
 * @version 1.0
 *
 * @see TxtReportTable example in TxtReportTable
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    /**
     * The index of the column.
     * */
    int id();

    /**
     * The inclusive start position of the text line.
     * */
    int start();

    /**
     * The exclusive start position of the text line.
     * */
    int end();
}
