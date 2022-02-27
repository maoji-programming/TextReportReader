package org.maoji.programming.textreportreader.model;

import  org.maoji.programming.textreportreader.TxtReportService;

/**
 * The config of TxtReportService
 *
 * @author Maoji
 * @version 1.0
 * @see TxtReportService
 *
 * */
public class TxtReportConfig {

    public String START = "~";
    public String END = "** END OF REPORT **";
    public static String SPACING_LINE_PATTERN = "[( )=-]+";
    public static String CURRENCIES_PATTERN = "[$]";
/**
 * Create the config of TxtReportService with custom starting word and ending word.
 *
 * <p>
 * keywords are not nullable
 * </p>
 * @param start starting keyword
 * @param end ending keyword
 */
    public TxtReportConfig(String start,String end){
        START = start == null ? START : start ;
        END = end == null ? END : end ;
    }


}
