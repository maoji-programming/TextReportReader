package org.maoji.programming.textreportreader.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define the model that is used for getting table records in .txt report files.
 *
 * <p>
 * Table is a common structure in the .txt report file, here is the example:
 * <pre class="code">
 * Name     Amount       Start Date     Married
 * --------------------------------------------
 * Chris    0.0          01-01-2022     Y
 * Mary     23.5         02-02-2022     N
 * ...
 * </pre>
 * </p>
 *
 * <p>
 * For another example, the model can be defined as below when handling the
 * <a href="https://www.ibm.com/docs/en/cmofi/7.4.0?topic=examples-example-one-bank-loan-report">bank loan report</a>.
 *
 * </p>
 *
 * <pre class="code">
 * &#064;TxtReportTable(StartKeyWord = "  NUMBER         NAME    ",EndKeyWord = " ** END OF REPORT ** ")
 * public class D94100100Table{
 *     &#064;Column(id = 1, start = 0 , end = 13)
 *     &#064;NumericValue(precision = 10)
 *     Integer loanNumber;
 *
 *     &#064;Column(id = 2, start = 13, end = 34)
 *     String customerName;
 *
 *     &#064;Column(id = 3, start = 34, end = 48)
 *     &#064;NumericValue(precision = 15, scale = 2)
 *     BigDecimal loanAmount;
 *
 *     //...
 *
 * }
 * </pre>
 *
 * @author Maoji
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TxtReportTable {
    /**
     * The starting key word of handling table records.
     * */
    String StartKeyWord() default  "** START OF TABLE **";

    /**
     * The ending key word of handling table records.
     * */
    String EndKeyWord() default "** END OF TABLE **";

}
