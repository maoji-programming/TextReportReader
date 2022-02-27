package org.maoji.programming.textreportreader;

import org.maoji.programming.textreportreader.exception.TxtReportException;
import org.maoji.programming.textreportreader.exception.TxtReportExceptionCode;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
/**
 * Report contains multiple pages
 *
 * @author Maoji
 * @version 1.0
 * */
@XmlRootElement(name = "report")
public class TxtReport<D,T> {
    private final static Logger LOGGER = Logger.getLogger(TxtReport.class.getName());
    Integer numOfPages = 0;

    private List<TxtReportPage> pages= new ArrayList();


    private final Class<D> dictClass;

    private final Class<T> tableClass;

    TxtReport(){
        dictClass = null;
        tableClass = null;
    }

    /**
     * @param d The class of defined dictionary.
     * @param t The class of defined table row.
     * */
    public TxtReport(Class<D> d, Class<T> t) {
        dictClass = d;
        tableClass = t;
    }
    /**
     * Create a new page.
     *
     * @throws TxtReportException failed to create a new page.
     * */
    void createPage() throws TxtReportException{
        try {
            TxtReportPage page = new TxtReportPage(dictClass, tableClass);
            pages.add(page);
            numOfPages++;
        }catch (Exception e){
            LOGGER.warning("Failed to create a new page.");
            throw new TxtReportException(TxtReportExceptionCode.TXT006,e);
        }

    }
    /**
     * Get the last page
     *
     * @return the last page if number of pages is not zero.
     * */
    public TxtReportPage getCurrentPage(){
        if(numOfPages != 0) return pages.get(numOfPages - 1);
        else return null;
    }

    @XmlElementWrapper(name="pages")
    @XmlElement(name="page")
    public List<TxtReportPage> getPages() {
        return pages;
    }

    Class<D> getDictClass() {
        return dictClass;
    }

    Class<T> getTableClass() {
        return tableClass;
    }



}

