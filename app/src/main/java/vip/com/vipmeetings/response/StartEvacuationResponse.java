package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "StartEvacuationResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class StartEvacuationResponse {


    @Element(name = "StartEvacuationResult", required = false)
    private String startEvacuationResult;

    public String getStartEvacuationResult() {
        return startEvacuationResult;
    }

    public void setStartEvacuationResult(String startEvacuationResult) {
        this.startEvacuationResult = startEvacuationResult;
    }
}
