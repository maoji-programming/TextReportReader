package org.maoji.programming.textreportreader;

import org.maoji.programming.textreportreader.exception.TxtReportException;

import java.util.logging.Logger;

/**
 * State of ending processing.
 *
 * @author Maoji
 * @version 1.0
 * */
class EndState implements StateI {
    public static final String name = "End Reading";
    private final static Logger LOGGER = Logger.getLogger(EndState.class.getName());
    @Override
    public void transit(TxtReportStateMachine fsm, String line) throws TxtReportException {

     }

    @Override
    public void handle(TxtReportStateMachine fsm, String line) {
        return;
    }
}
