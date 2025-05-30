package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "GetEvacuationResponse", strict = false)
//@Namespace(reference = "http://tempuri.org/")
public class GetEvacuationInformationResponse {


    public GetEvacuationResult getGetEvacuationResult() {
        return getEvacuationResult;
    }

    public void setGetEvacuationResult(GetEvacuationResult getEvacuationResult) {
        this.getEvacuationResult = getEvacuationResult;
    }

    @Element(name = "GetEvacuationResult", required = false)
    private GetEvacuationResult getEvacuationResult;




}
