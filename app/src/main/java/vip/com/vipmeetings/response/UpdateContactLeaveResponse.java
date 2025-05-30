package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "UpdateContactLeaveResponse", strict = false)
//@Namespace(reference = "http://tempuri.org/")
public class UpdateContactLeaveResponse {


    @Element(name = "UpdateContactLeaveResult", required = false)
    private UpdateContactLeaveResult updateContactLeaveResult;

    public UpdateContactLeaveResult getUpdateContactLeaveResult() {
        return updateContactLeaveResult;
    }

    public void setUpdateContactLeaveResult(UpdateContactLeaveResult updateContactLeaveResult) {
        this.updateContactLeaveResult = updateContactLeaveResult;
    }
}
