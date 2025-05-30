package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "SetClearTimeToEvacuationsResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class SetClearTimeToEvacuationsResponse {


    @Element(name = "SetClearTimeToEvacuationsResult", required = false)
    private String setClearTime;

    public String getSetClearTime() {
        return setClearTime;
    }

    public void setSetClearTime(String setClearTime) {
        this.setClearTime = setClearTime;
    }
}
