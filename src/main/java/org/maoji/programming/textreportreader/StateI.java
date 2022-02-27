package org.maoji.programming.textreportreader;

import org.maoji.programming.textreportreader.exception.TxtReportException;

/**
 * Every state must implement <code>StateI</code> which include the transitions and handling content
 *
 * */

interface StateI {

    /**
     * Transition of the state
     *
     * @param fsm the finite state machine
     * @param line the text line
     *
     * @throws TxtReportException throws during the transition
     * */
    void transit(TxtReportStateMachine fsm, String line) throws TxtReportException;
    /**
     * action content of the state
     *
     * @param fsm the finite state machine
     * @param line the text line
     *
     * @throws TxtReportException throws during the handling
     * */
    void handle( TxtReportStateMachine fsm ,String line) throws TxtReportException;

}
