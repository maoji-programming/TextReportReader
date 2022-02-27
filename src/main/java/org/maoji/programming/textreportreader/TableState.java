package org.maoji.programming.textreportreader;

import org.maoji.programming.textreportreader.annotation.Column;
import org.maoji.programming.textreportreader.annotation.TxtReportTable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Logger;
/**
 * State of capturing all fields of every row in the table.
 *
 * @author Maoji
 * @version 1.0
 * */
class TableState implements StateI {
    public final static String name = "Reading table";
    private final static Logger LOGGER = Logger.getLogger(TableState.class.getName());
    private Boolean isFirst = true;

    //private Class cls;
    //public TableState(Class cls){
    //    this.cls = cls;
    //}

    @Override
    public void transit(TxtReportStateMachine fsm, String line) {

        Annotation anno = fsm.getReport().getTableClass().getAnnotation(TxtReportTable.class);

        if(line.contains(fsm.setting.START)){
            fsm.getReport().createPage();
            fsm.setState(new DictionaryState());

        }else if(line.contains(fsm.setting.END)){
            fsm.setState(new EndState());
        }else if(anno != null) {
            if (line.contains(((TxtReportTable) anno).EndKeyWord())) {
                fsm.setState(new DictionaryState());

            }
        }
    }

    @Override
    public void handle(TxtReportStateMachine fsm, String line) {

        try {
            Object tableRow = fsm.getReport().getTableClass().getDeclaredConstructor().newInstance();

            if(isFirst){
                isFirst =false;
                return;
            }
            //Skip the decoration line e.g. "======="
            if(line.matches(fsm.setting.SPACING_LINE_PATTERN)) return;
            for(Field fd: fsm.getReport().getTableClass().getDeclaredFields()) {
                fd.setAccessible(true);

                Integer colStart = fd.getDeclaredAnnotation(Column.class).start();
                Integer colEnd = fd.getDeclaredAnnotation(Column.class).end();

                String colValue = line.substring(colStart,colEnd).trim();

                if (fd.getType().equals(String.class)) {
                    fd.set(tableRow, colValue);
                } else if (fd.getType().equals(Integer.class)) {
                    fd.set(tableRow, TxtReportDataUtil.integerNumber(fd, colValue));
                } else if (fd.getType().equals(Long.class)) {
                    fd.set(tableRow, TxtReportDataUtil.longNumber(fd, colValue));
                } else if (fd.getType().equals(Double.class)) {
                    fd.set(tableRow, TxtReportDataUtil.doubleNumber(fd, colValue));
                } else if (fd.getType().equals(Float.class)) {
                    fd.set(tableRow, TxtReportDataUtil.floatNumber(fd, colValue));
                } else if (fd.getType().equals(BigDecimal.class)) {
                    fd.set(tableRow, TxtReportDataUtil.bigDecimalNumber(fd, colValue));
                } else if (fd.getType().equals(Date.class)) {
                    fd.set(tableRow, TxtReportDataUtil.dateValue(fd, colValue));
                } else if (fd.getType().equals(Boolean.class)) {
                    fd.set(tableRow, TxtReportDataUtil.booleanValue(fd, colValue));
                } else {
                    throw new IllegalAccessException("Unknown data type.");
                }
            }
            fsm.getReport().getCurrentPage().getTable().add(tableRow);
        } catch (IllegalAccessException | NoSuchMethodException e) {
            LOGGER.warning(String.format("Field issues in line %d.",fsm.getCount()));
        } catch (InvocationTargetException e) {
            LOGGER.warning(String.format("Field issues in line %d.",fsm.getCount()));
        } catch (InstantiationException e) {
            LOGGER.warning(String.format("Field issues in line %d.",fsm.getCount()));
        } catch (StringIndexOutOfBoundsException e){
            //no action if out of bound
            LOGGER.warning(String.format("Out of field bound in line %d.",fsm.getCount()));
        }


        return;

    }
}
