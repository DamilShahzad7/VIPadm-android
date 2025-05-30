package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import vip.com.vipmeetings.models.Empty;
import vip.com.vipmeetings.models.Error;
import vip.com.vipmeetings.models.RecordContact;

@Root(name = "GetExcelDataResult", strict = false)
public class GetExcelDataResult {


    @ElementList(entry = "Records", inline = true, required = false)
    public List<RecordContact> recordContacts;

    @Element(name = "Error", required = false)
    public Error error;


    @Element(name = "Empty", required = false)
    private Empty empty;


    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public List<RecordContact> getRecordContacts() {
        return recordContacts;
    }

    public void setRecordContacts(List<RecordContact> recordContacts) {
        this.recordContacts = recordContacts;
    }

    public Empty getEmpty() {
        return empty;
    }

    public void setEmpty(Empty empty) {
        this.empty = empty;
    }
}
