package org.maoji.programming.textreportreader;

import org.maoji.programming.textreportreader.exception.TxtReportException;

import java.util.logging.Logger;

/**
 * State of pending for retrieving data
 *
 * @author Maoji
 * @version 1.0
 * */
class PendingState implements StateI{
    public static final String name = "Start Reading";
    private final static Logger LOGGER = Logger.getLogger(PendingState.class.getName());

    @Override
    public void transit(TxtReportStateMachine fsm, String line) throws TxtReportException {
        if(line.contains(fsm.setting.START)){
            // create new page
            fsm.getReport().createPage();
            // change to dictionary state;
            fsm.setState(new DictionaryState());
        }
     }

    @Override
    public void handle(TxtReportStateMachine fsm, String line) {
        return;
    }
}
