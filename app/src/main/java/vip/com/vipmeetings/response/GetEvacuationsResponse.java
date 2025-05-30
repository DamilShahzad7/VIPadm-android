package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "GetEvacuationsResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetEvacuationsResponse {


    @Element(name = "GetEvacuationsResult", required = false)
    private GetEvacuationsResult getEvacuationsResult;

    public GetEvacuationsResult getGetEvacuationsResult() {
        return getEvacuationsResult;
    }

    public void setGetEvacuationsResult(GetEvacuationsResult getEvacuationsResult) {
        this.getEvacuationsResult = getEvacuationsResult;
    }
}
