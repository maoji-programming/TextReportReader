package org.maoji.programming.textreportreader;

import org.maoji.programming.textreportreader.annotation.Pair;
import org.maoji.programming.textreportreader.annotation.TxtReportTable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Logger;
import java.util.regex.Pattern;
/**
 * State of capturing dictionary pairs
 *
 * @author Maoji
 * @version 1.0
 * */
class DictionaryState implements StateI {
    public static String name = "Reading Dictionary";
    private final static Logger LOGGER = Logger.getLogger(DictionaryState.class.getName());

    @Override
    public void transit(TxtReportStateMachine fsm, String line) {
        Annotation anno = fsm.getReport().getTableClass().getAnnotation(TxtReportTable.class);
        if(line.contains(fsm.setting.START)){
            fsm.getReport().createPage();
            fsm.setState(new DictionaryState());

        }else if(line.contains(fsm.setting.END)){
            fsm.setState(new EndState());

        }else if(anno != null) {
            if(line.contains(((TxtReportTable)anno).StartKeyWord())){
                fsm.setState(new TableState());
            }
        }


    }

    @Override
    public void handle(TxtReportStateMachine fsm, String line){

        for(Field fd: fsm.getReport().getCurrentPage().getDictionary().getClass().getDeclaredFields()){
            if(fd.getDeclaredAnnotation(Pair.class) != null) {
                fd.setAccessible(true);
                String dictKey = fd.getDeclaredAnnotation(Pair.class).key();
                Integer dictValueLength = fd.getDeclaredAnnotation(Pair.class).valueLength();

                if (line.contains(dictKey)) {
                    try {
                        String dictValue = line.split(dictKey)[1].substring(0, dictValueLength).trim();
                        if(!Pattern.matches(fd.getDeclaredAnnotation(Pair.class).valuePattern(),dictValue)) {
                            continue;
                        }
                        if (fd.getType().equals(String.class)) {
                            fd.set(fsm.getReport().getCurrentPage().getDictionary(), dictValue);
                        } else if (fd.getType().equals(Integer.class)) {
                            fd.set(fsm.getReport().getCurrentPage().getDictionary(), TxtReportDataUtil.integerNumber(fd, dictValue));
                        } else if (fd.getType().equals(Long.class)) {
                            fd.set(fsm.getReport().getCurrentPage().getDictionary(), TxtReportDataUtil.longNumber(fd, dictValue));
                        } else if (fd.getType().equals(Double.class)) {
                            fd.set(fsm.getReport().getCurrentPage().getDictionary(), TxtReportDataUtil.doubleNumber(fd, dictValue));
                        } else if (fd.getType().equals(Float.class)) {
                            fd.set(fsm.getReport().getCurrentPage().getDictionary(), TxtReportDataUtil.floatNumber(fd, dictValue));
                        } else if (fd.getType().equals(BigDecimal.class)) {
                            fd.set(fsm.getReport().getCurrentPage().getDictionary(), TxtReportDataUtil.bigDecimalNumber(fd, dictValue));
                        } else if (fd.getType().equals(Date.class)) {
                            fd.set(fsm.getReport().getCurrentPage().getDictionary(), TxtReportDataUtil.dateValue(fd, dictValue));
                        } else if (fd.getType().equals(Boolean.class)) {
                            fd.set(fsm.getReport().getCurrentPage().getDictionary(), TxtReportDataUtil.booleanValue(fd, dictValue));
                        } else {
                            throw new IllegalAccessException("Unknown data type.");
                        }
                    } catch (IllegalAccessException e) {
                        LOGGER.warning(String.format("Field issues in line %d.",fsm.getCount()));
                    } catch (StringIndexOutOfBoundsException e){
                        //no action if out of bound
                        LOGGER.warning(String.format("Out of field bound in line %d.",fsm.getCount()));
                    }
                }
            }
        }


        return;
    }
}
