package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "DeleteContactLeaveResponse", strict = false)
//@Namespace(reference = "http://tempuri.org/")
public class DeleteContactLeaveResponse {


    @Element(name = "DeleteContactLeaveResult", required = false)
    private DeleteContactLeaveResult deleteContactLeaveResult;

    public DeleteContactLeaveResult getDeleteContactLeaveResult() {
        return deleteContactLeaveResult;
    }

    public void setDeleteContactLeaveResult(DeleteContactLeaveResult deleteContactLeaveResult) {
        this.deleteContactLeaveResult = deleteContactLeaveResult;
    }
}
