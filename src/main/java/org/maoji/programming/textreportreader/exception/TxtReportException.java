package org.maoji.programming.textreportreader.exception;


/**
 * Exception thrown during service initialization and retrieving.
 *
 * @author Maoji
 * @version 1.0
 */
public class TxtReportException extends RuntimeException {
    /**
     * Create a new {@code TxtReportException}
     * with the message.
     *
     * @param message the detail message
     */
    public TxtReportException(String message) {
        super(message);
    }
    /**
     * Create a new {@code TxtReportException}
     * with the exception code.
     *
     * @param code the exception code in {@link TxtReportExceptionCode}
     */
    public TxtReportException(TxtReportExceptionCode code) {
        super(code.message);
    }
    /**
     * Create a new {@code TxtReportException}
     * with the message and the root cause.
     *
     * @param msg the detail message
     * @param throwable the root cause
     * @see TxtReportExceptionCode
     */
    public TxtReportException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
    /**
     * Create a new {@code TxtReportException}
     * with the exception code and the root cause.
     *
     * @param code the exception code in {@link TxtReportExceptionCode}
     * @param throwable the root cause
     * @see TxtReportExceptionCode
     */
    public TxtReportException(TxtReportExceptionCode code, Throwable throwable) {
        super(code.message, throwable);
    }
}
