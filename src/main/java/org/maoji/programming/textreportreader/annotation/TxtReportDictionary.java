package org.maoji.programming.textreportreader.annotation;

import javax.xml.bind.annotation.XmlRootElement;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declare the model is used for getting key/value pair in .txt report files.
 *
 * <p>
 * Dictionary pair is a common structure in the .txt report file, here is the example:
 * <pre class="code">
 * Name:       Chris
 * Amount:     0.0
 * Start Date: 01-01-2022
 * Married:    Y
 * ...
 * </pre>
 * </p>
 *
 * <p>
 * for another example, the model can be defined as below when handling the
 * <a href="https://www.ibm.com/docs/en/cmofi/7.4.0?topic=examples-example-one-bank-loan-report">bank loan report</a>.
 *
 * </p>
 *
 * <pre class="code">
 * &#064;TxtReportDictionary
 * public class D94100100Dict {
 *
 *     &#064;XmlElement
 *     &#064;Pair(key ="PAGE  ",valueLength = 4, valuePattern = ".*")
 *     &#064;NumericValue(precision = 4, scale = 0)
 *     Integer page;
 *
 *     &#064;XmlElement
 *     &#064;Pair(key = "DATE  ",valueLength = 8, valuePattern = ".*")
 *     &#064;DateValue(format = "MM-DD-YY")
 *     Date date;
 *
 *     &#064;XmlElement
 *     &#064;Pair(key = "REPORT   ",valueLength = 9, valuePattern = "[a-zA-Z0-9]{9}")
 *     String reportId;
 * }
 * </pre>
 *
 * @author Maoji
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@XmlRootElement
public @interface TxtReportDictionary {
}
