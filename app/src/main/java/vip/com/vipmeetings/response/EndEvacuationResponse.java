package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "EndEvacuationResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class EndEvacuationResponse {


    @Element(name = "EndEvacuationResult", required = false)
    private String endEvacuationResult;

    public String getEndEvacuationResult() {
        return endEvacuationResult;
    }

    public void setEndEvacuationResult(String endEvacuationResult) {
        this.endEvacuationResult = endEvacuationResult;
    }
}
