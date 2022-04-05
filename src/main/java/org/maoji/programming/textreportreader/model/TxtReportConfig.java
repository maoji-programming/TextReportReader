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
    public TxtReportType TYPE = TxtReportType.REGULAR;
    public static String SPACING_LINE_PATTERN = "[( )=-]+";
    public static String CURRENCIES_PATTERN = "[$]";
/**
 * Create the config of TxtReportService with custom starting word and ending word.
 *
 */

    private TxtReportConfig(){

    }

    public static TxtReportConfig getConfig(){
        return new TxtReportConfig();
    }
    /**
     * Create the config of TxtReportService with custom ending word.
     *
     * <p>
     * keywords are not nullable
     * </p>
     * @param start starting keyword
     */
    public TxtReportConfig setStart(String start){
        START = start == null ? START : start ;
        return this;
    }

    /**
     * Create the config of TxtReportService with custom ending word.
     *
     * <p>
     * keywords are not nullable
     * </p>
     * @param end ending keyword
     */
    public TxtReportConfig setEnd(String end) {
        END = end == null ? END : end ;
        return this;
    }

    /**
     * Create the config of TxtReportService with used type.
     *
     * <p>
     * keywords are not nullable
     * </p>
     * @param type report type
     */
    public TxtReportConfig setReportType(TxtReportType type){
        TYPE = type == null ? TYPE : type ;
        return this;
    }

}
