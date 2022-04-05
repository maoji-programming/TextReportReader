package org.maoji.programming.textreportreader;

import org.maoji.programming.textreportreader.annotation.Column;
import org.maoji.programming.textreportreader.annotation.TxtReportTable;
import org.maoji.programming.textreportreader.exception.TxtReportException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Logger;

/**
 * State of pending for retrieving data
 *
 * @author Maoji
 * @version 1.0
 * */
class PendingTableState implements StateI{
    public static final String name = "Start Reading";
    private final static Logger LOGGER = Logger.getLogger(PendingTableState.class.getName());

    @Override
    public void transit(TxtReportStateMachine fsm, String line) throws TxtReportException {
        Annotation anno = fsm.getReport().getTableClass().getAnnotation(TxtReportTable.class);
        if(anno != null) {
            if(line.contains(((TxtReportTable)anno).StartKeyWord())) {
                // create new page
                fsm.getReport().createPage();
                // change to dictionary state;
                fsm.setState(new TableState().ignoreFirstRow(false));
            }
        }
     }

    @Override
    public void handle(TxtReportStateMachine fsm, String line) {
        return;
    }
}
