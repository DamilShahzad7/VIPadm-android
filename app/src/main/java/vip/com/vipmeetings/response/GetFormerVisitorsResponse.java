package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "GetFormerVisitorsResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetFormerVisitorsResponse {


    @Element(name = "GetFormerVisitorsResult", required = false)
    private GetFormerVisitorsResult getFormerVisitorsResult;

    public GetFormerVisitorsResult getGetFormerVisitorsResult() {
        return getFormerVisitorsResult;
    }

    public void setGetFormerVisitorsResult(GetFormerVisitorsResult getFormerVisitorsResult) {
        this.getFormerVisitorsResult = getFormerVisitorsResult;
    }
}
