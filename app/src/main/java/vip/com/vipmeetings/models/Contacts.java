package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Srinath on 30/05/17.
 */

@Root(strict = false)
public class Contacts {


    @Element(name = "Status",required=false)
    public Status Status;
    @ElementList(entry = "Contact", inline = true,required = false)
    public List<Contact> Contact;




    public Status getStatus() {
        return Status;
    }

    public void setStatus(Status status) {
        Status = status;
    }

    public List<Contact> getContact() {
        return Contact;
    }

    public void setContact(List<Contact> contact) {
        Contact = contact;
    }
}
