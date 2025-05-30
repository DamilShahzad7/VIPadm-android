package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "GetEvacuationResponsiblesResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetEvacuationResponsiblesResponse {


    @Element(name = "GetEvacuationResponsiblesResult", required = false)
    private GetEvacuationResponsiblesResult getEvacuationResponsiblesResult;

    public GetEvacuationResponsiblesResult getGetEvacuationResponsiblesResult() {
        return getEvacuationResponsiblesResult;
    }

    public void setGetEvacuationResponsiblesResult(GetEvacuationResponsiblesResult getEvacuationResponsiblesResult) {
        this.getEvacuationResponsiblesResult = getEvacuationResponsiblesResult;
    }
}
