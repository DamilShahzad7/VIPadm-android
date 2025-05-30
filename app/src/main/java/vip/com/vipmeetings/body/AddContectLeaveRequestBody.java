package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.AddContactLeave;
import vip.com.vipmeetings.request.UpdateContactLeave;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class AddContectLeaveRequestBody {


    @Element(name = "AddContactLeave", required = false)
    private AddContactLeave addContactLeave;

    public AddContactLeave getAddContactLeave() {
        return addContactLeave;
    }

    public void setAddContactLeave(AddContactLeave addContactLeave) {
        this.addContactLeave = addContactLeave;
    }
}
