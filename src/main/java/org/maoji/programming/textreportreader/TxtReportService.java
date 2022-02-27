package org.maoji.programming.textreportreader;

import org.maoji.programming.textreportreader.annotation.Column;
import org.maoji.programming.textreportreader.annotation.Pair;
import org.maoji.programming.textreportreader.annotation.TxtReportDictionary;
import org.maoji.programming.textreportreader.annotation.TxtReportTable;
import org.maoji.programming.textreportreader.annotation.value.BooleanValue;
import org.maoji.programming.textreportreader.annotation.value.DateValue;
import org.maoji.programming.textreportreader.annotation.value.NumericValue;
import javax.xml.bind.annotation.XmlElement;

import org.maoji.programming.textreportreader.exception.TxtReportException;
import org.maoji.programming.textreportreader.exception.TxtReportExceptionCode;
import org.maoji.programming.textreportreader.model.TxtReportConfig;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Main service of retrieving data from .txt report file.
 *
 * <p>
 * Given a dictionary model and a table model, the program can retrieve the data
 * from the raw text report. To use the service, configuration must be defined first,
 * <pre class="code">
 * TxtReportConfig config = new TxtReportConfig("\f","** END OF REPORT **");
 * </pre>
 * assume that the report contains multiple pages, there is a starting keyword <code>{@link TxtReportTable#StartKeyWord}</code> for
 * splitting pages and end the report by an ending keyword <code>{@link TxtReportTable#EndKeyWord}</code> at the last line.
 * </p>
 * <p>
 * To build the dictionary model(POJO), the annotation <code>{@link Pair}</code> is required to add.
 * To build the table model(POJO), the annotation <code>{@link Column}</code> is required to add.
 * </p>
 * <p>
 * Depends on the datatype of the field, the annotation is required to use:
 * <table border="1">
 *     <thead>
 *          <tr>
 *              <th>type</th>
 *              <th>annotation</th>
 *              <th>instances</th>
 *          </tr>
 *     </thead>
 *     <tbody>
 *          <tr>
 *              <td>date</td>
 *              <td><code>{@link DateValue}</code></td>
 *              <td><code>Date</code></td>
 *          </tr>
 *          <tr>
 *              <td>number</td>
 *              <td><code>{@link NumericValue}</code></td>
 *              <td><code>BigDecimal, Integer, Long, Float, Double</code></td>
 *          </tr>
 *          <tr>
 *              <td>boolean</td>
 *              <td><code>{@link BooleanValue}</code></td>
 *              <td><code>Boolean</code></td>
 *          </tr>
 *     </tbody>
 * </table>
 * </p>
 * <p>
 * to export the .xml file of the .txt report. the annotation <code>XmlElement</code>should be added in the fields.
 * Please check the <a href="https://docs.oracle.com/javase/8/docs/api/javax/xml/bind/annotation/XmlElement.html">document</a>
 * </p>
 * <p>
 * to create the service object,
 * <pre class="code">
 * TxtReportService service = new TxtReportService(
 *                     theDict.class,
 *                     theTable.class,
 *                     "/to/the/filepath",
 *                     config);
 * </pre>
 * </p>
 *
 * @author Maoji
 * @version 1.0
 *
 * @param <D> the instance of the dictionary, which must be <code>TxtReportDictionary</code>.
 * @param <T> the instance of the table row, which must be <code>TxtReportTable</code>.
 * @see TxtReportDictionary
 * @see TxtReportTable
 * @see javax.xml.bind.annotation.XmlElement;
 */
public final class TxtReportService<D extends TxtReportDictionary,T extends TxtReportTable> {
    private static final Logger LOGGER = Logger.getLogger(TxtReportService.class.getName());

    private final Class<D> dictClass;
    private final Class<T> tableClass;

    private String filepath;
    private TxtReportConfig setting;

    private TxtReportStateMachine stateMachine;

    /**
     * Create the object
     * <p>
     * Validate the instance of dictionary and table before retrieve report.
     * </p>
     * @param dictClass the class of dictionary
     * @param tableClass the class of table
     * @param path the filepath
     * @param config the configuration
     * @throws TxtReportException throws the exception during the validation, processing
     * */
    public TxtReportService(Class<D> dictClass, Class<T> tableClass, String path, TxtReportConfig config) throws TxtReportException {

            this.dictClass = dictClass;
            this.tableClass = tableClass;
            this.filepath = path;
            this.setting = config;

            //if report file include dict
            if(this.dictClass != null) {
                TxtReportExceptionCode code = validateDict(this.dictClass);
                if (!code.equals(TxtReportExceptionCode.TXT000)) {
                    throw new TxtReportException(code);
                }
            }else{
                throw new TxtReportException(TxtReportExceptionCode.TXT001);
            }
            //if report file includes table
            if(this.tableClass != null) {
                TxtReportExceptionCode code = validateTable(this.tableClass);
                if (!code.equals(TxtReportExceptionCode.TXT000)) {
                    throw new TxtReportException(code);
                }
            }else{
                throw new TxtReportException(TxtReportExceptionCode.TXT002);
            }

            initializeStateMap();
            retrieve();

    }

    private TxtReportExceptionCode validateDict(Class cls){

        if(cls.getAnnotation(TxtReportTable.class) == null && cls.getAnnotation(TxtReportDictionary.class) != null){
            for(Field fd : cls.getFields()){
                // Skip if the parameter is not the pair field.
                if(fd.getAnnotation(Pair.class) == null) continue;
                if(validateNumeric(fd) || validateDate(fd) || validateBoolean(fd)){
                    continue;
                }else{
                    return TxtReportExceptionCode.TXT004;
                }
            }
            return TxtReportExceptionCode.TXT000;
        }
        return TxtReportExceptionCode.TXT001;
    }

    private TxtReportExceptionCode validateTable(Class cls){

        if(cls.getAnnotation(TxtReportTable.class) != null && cls.getAnnotation(TxtReportDictionary.class) == null){
            for(Field fd : cls.getFields()){
                // Skip if the parameter is not the column field.
                if(fd.getAnnotation(Column.class) == null) continue;
                if(fd.getAnnotation(Column.class).start() >= fd.getAnnotation(Column.class).end()) return TxtReportExceptionCode.TXT003;
                if(validateNumeric(fd) || validateDate(fd) || validateBoolean(fd)){
                    continue;
                }else{
                    return TxtReportExceptionCode.TXT004;
                }
            }
            return TxtReportExceptionCode.TXT000;
        }
        return TxtReportExceptionCode.TXT002;
    }

    private boolean validateNumeric(Field fd){
        return (fd.getType() == BigDecimal.class || fd.getType() == Double.class || fd.getType() == Float.class) && fd.getAnnotation(NumericValue.class) == null;
    }

    private boolean validateDate(Field fd){
        return fd.getType() == Date.class && fd.getAnnotation(DateValue.class) != null;
    }

    private boolean validateBoolean(Field fd){
        return fd.getType() == Boolean.class && fd.getAnnotation(BooleanValue.class) != null;
    }

    private void initializeStateMap(){
        stateMachine = new TxtReportStateMachine(setting);
        stateMachine.setReport(dictClass,tableClass);
    }

    private void retrieve() throws TxtReportException{

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath))){
            String buffer = "";
            while((buffer = bufferedReader.readLine()) != null){
                stateMachine.updateState(buffer);
                stateMachine.executeState(buffer);
                stateMachine.addCount();
            }
        }catch (IOException e) {
            throw new TxtReportException(TxtReportExceptionCode.TXT005, e);
        }

    }
    /**
     * Retrieve the report in .josn file
     * @param filepath location of output xml file
     * @throws TxtReportException failed to export the xml file
     */
    public void retrieveJson(String filepath)throws TxtReportException{
    }

    /**
     * Retrieve the report in .xml file
     * @param filepath location of output xml file
     * @throws TxtReportException failed to export the xml file
     */
    public void retrieveXml(String filepath) throws TxtReportException {
        try{
            File xmlFile = new File(filepath);
            JAXBContext jaxbContext = JAXBContext.newInstance(new Class[] {TxtReport.class, dictClass, tableClass});
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
            marshaller.marshal(stateMachine.getReport(),xmlFile);
        } catch (JAXBException e) {
            throw new TxtReportException(TxtReportExceptionCode.TXT007, e);
        }


    }
    /**
     * Retrieve the report
     *
     * @return report
     * */
    public TxtReport retrieveTxtReport(){
        return this.stateMachine.report;
    }


}