package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "UpdateClearTimeToEvacuationResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class UpdateClearTimeToEvacuationResponse {


    @Element(name = "UpdateClearTimeToEvacuationResult", required = false)
    private String updateClearTime;

    public String getUpdateClearTime() {
        return updateClearTime;
    }

    public void setUpdateClearTime(String updateClearTime) {
        this.updateClearTime = updateClearTime;
    }
}
