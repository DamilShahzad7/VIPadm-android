package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "GetEvacuationStatusResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetEvacuationStatusResponse {


    @Element(name = "GetEvacuationStatusResult", required = false)
    private GetEvacuationStatusResult getEvacuationStatusResult;

    public GetEvacuationStatusResult getGetEvacuationStatusResult() {
        return getEvacuationStatusResult;
    }

    public void setGetEvacuationStatusResult(GetEvacuationStatusResult getEvacuationStatusResult) {
        this.getEvacuationStatusResult = getEvacuationStatusResult;
    }
}
