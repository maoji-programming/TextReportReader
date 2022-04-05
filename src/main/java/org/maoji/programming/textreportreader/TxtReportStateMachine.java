package org.maoji.programming.textreportreader;

import org.maoji.programming.textreportreader.annotation.*;
import org.maoji.programming.textreportreader.exception.TxtReportException;
import org.maoji.programming.textreportreader.model.TxtReportConfig;

import java.util.logging.Logger;

/**
 * Program that controlling the states
 *
 * <p>
 * to retrieve all the data in the .txt report file, the machine basically includes
 * four states:
 * <ol>
 *     <li>Pending state</li>
 *     <li>Dictionary state</li>
 *     <li>Table state</li>
 *     <li>End state</li>
 * </ol>
 * Initially, the machine starts at the pending state.
 * </p>
 * <p>
 * For each state, the machine preforms state transition (see {@link StateI#transit})
 * first, then it executes the content of the status (see {@link StateI#handle}).
 * Finally, the line count increased by 1 before go to the next line.
 * </p>
 * <p>
 * When the machine is in the pending state, it performs nothing. While the machine is
 * reading the file line by line. It is waiting for the starting keyword (see {@link TxtReportConfig#START})
 * appearing in the text line. If the line contains the keyword, it creates new page
 * and goes to the dictionary state.
 * </p>
 * <p>
 * When the machine is in the dictionary state, it retrieve the data of pairs in the
 * dictionary model. It retrieve the value by the length and pattern defined in the field
 * of dictionary model if the line matches the key of the field. If the current line contains
 * the table starting keyword (see {@link TxtReportTable#StartKeyWord}), the machine goes
 * to table state. If the line contains the starting keyword, it creates new page and
 * goes to the dictionary state. If the line contains the starting keyword, it creates
 * new page. If the line contains the ending keyword (see {@link TxtReportConfig#END}),
 * it goes to the end state.
 * </p>
 * <p>
 * When the machine is in the table state, it retrieve each field of the table row.
 * It retrieves the value by start and end positions defined in the field in table model.
 * If the current line contains the table ending keyword (see {@link TxtReportTable#EndKeyWord}),
 * the machine goes to dictionary state.If the line contains the starting keyword, it creates
 * new page. If the line contains the ending keyword (see {@link TxtReportConfig#END}),
 * it goes to the end state.
 * </p>
 * <p>
 * When the machine is in the end state, it performs nothing. Also, it does not any transition.
 * As it is a sink state.
 * </p>
 * @author Maoji
 * @version 1.0
 *
 * @see TxtReportConfig
 * @see TxtReportTable
 * @see TxtReportDictionary
 * @see StateI
 * @see PendingState
 * @see DictionaryState
 * @see TableState
 * @see EndState
 */
class TxtReportStateMachine {

    private final static Logger LOGGER = Logger.getLogger(TxtReportStateMachine.class.getName());
    private StateI state;
    private Integer count = 0;
    TxtReport report;
    TxtReportConfig setting;
    /**
     * Create <code>TxtReportStateMachine</code> with configuration
     *
     * @param setting the configuration
     *
     * */
    TxtReportStateMachine(TxtReportConfig setting){
        this.setting = setting;
        this.setInitialState();

    }
    /**
     * set the initial state based on the setting
     *
     * */
    private void setInitialState(){
        switch (setting.TYPE){
            case TABLE_TEXT:
                this.state = new PendingTableState();
                break;
            case REGULAR:
            default:
                this.state = new PendingState();
                break;
        }
    }
    /**
     * Control the status transition
     *
     * @throws TxtReportException throws during the transition
     * */
    void updateState(String row) throws TxtReportException{
        state.transit(this, row);

    }
    /**
     * Control the status action
     *
     * @throws TxtReportException throws during the handling
     * */
    void executeState(String row) throws TxtReportException {
        state.handle(this, row);

    }

    public TxtReport getReport() {
        return report;
    }
    /**
     * Create new report.
     *
     * @param d The class of defined dictionary.
     * @param t The class of defined table row.
     * */
    <D,T>void setReport(Class<D> d, Class<T> t){
        report = new TxtReport(d,t);
    }

    public StateI getState() {
        return state;
    }


    public void setState(StateI state) {
        this.state = state;
    }
    /**
     * increment of line count
     * */
    void addCount(){
        count++;
    }

    Integer getCount() {
        return count;
    }
}
