package org.maoji.programming.textreportreader.exception;

/**
 * list of all exception codes
 *
 * @author Maoji
 * @version 1.0
 */
public enum TxtReportExceptionCode {
    TXT000("No Exception."),
    TXT001("Invalid model for TxtReportDictionary."),
    TXT002("Invalid model for TxtReportTable."),
    TXT003("Invalid bound for the column."),
    TXT004("Insufficient annotations for the column/field."),
    TXT005("Input file does not exist."),
    TXT006("Failed to create a new page."),
    TXT007("Failed to retrieve xml file."),
    TXT008("Failed to retrieve json file.")
    ;

    String message;

    TxtReportExceptionCode(String msg) {
        message = msg;
    }

    public String getMessage() {
        return message;
    }
}
