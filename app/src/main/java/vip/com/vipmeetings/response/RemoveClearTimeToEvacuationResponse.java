package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "RemoveClearTimeToEvacuationResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class RemoveClearTimeToEvacuationResponse {


    @Element(name = "RemoveClearTimeToEvacuationResult", required = false)
    private String removeClearTime;

    public String getRemoveClearTime() {
        return removeClearTime;
    }

    public void setRemoveClearTime(String removeClearTime) {
        this.removeClearTime = removeClearTime;
    }
}
