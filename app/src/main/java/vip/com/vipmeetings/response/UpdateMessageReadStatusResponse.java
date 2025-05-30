package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "UpdateMessageReadStatusResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class UpdateMessageReadStatusResponse {


    @Element(name = "UpdateMessageReadStatusResult", required = false)
    private String updateMessageReadStatusResult;

    public String getUpdateMessageReadStatusResult() {
        return updateMessageReadStatusResult;
    }

    public void setUpdateMessageReadStatusResult(String updateMessageReadStatusResult) {
        this.updateMessageReadStatusResult = updateMessageReadStatusResult;
    }
}
