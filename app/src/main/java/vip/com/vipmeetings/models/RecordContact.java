package vip.com.vipmeetings.models;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(strict = false)
public class RecordContact {


    @ElementList(entry = "Record", inline = true,required = false)
    public List<Contact> recordContacts;

    public List<Contact> getRecordContacts() {
        return recordContacts;
    }

    public void setRecordContacts(List<Contact> recordContacts) {
        this.recordContacts = recordContacts;
    }
}
