package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "AddContactLeaveResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class AddContactLeaveResponse {


    @Element(name = "AddContactLeaveResult", required = false)
    private AddContactLeaveResult addContactLeaveResult;

    public AddContactLeaveResult getAddContactLeaveResult() {
        return addContactLeaveResult;
    }

    public void setAddContactLeaveResult(AddContactLeaveResult addContactLeaveResult) {
        this.addContactLeaveResult = addContactLeaveResult;
    }
}
