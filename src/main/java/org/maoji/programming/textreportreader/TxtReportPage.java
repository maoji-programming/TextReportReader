package org.maoji.programming.textreportreader;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Page contains a dictionary and a table
 *
 * @author Maoji
 * @version 1.0
 * */
@XmlRootElement
public class TxtReportPage<D,T> {
    private final static Logger LOGGER = Logger.getLogger(TxtReportPage.class.getName());
    private D dictionary;
    private List<T> table;

    TxtReportPage(){
        dictionary = null;
        table = null;
    }
    /**
     * @param dictClass The class of defined dictionary.
     * @param tableClass The class of defined table row.
     * */

    TxtReportPage(Class<D> dictClass, Class<T> tableClass) throws Exception{
        dictionary = dictClass.getDeclaredConstructor().newInstance();
        table = new ArrayList<>();
    }

    @XmlElement
    public D getDictionary() {
        return dictionary;
    }

    @XmlElementWrapper(name = "table")
    @XmlElement(name = "row")
    public List<T> getTable() {
        return table;
    }


}
