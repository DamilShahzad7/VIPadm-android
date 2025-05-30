package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.GetEvacuationInformationResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetEvacuationInformationResponseBody {

    public GetEvacuationInformationResponse getGetEvacuationInformationResponse() {
        return getEvacuationInformationResponse;
    }

    public void setGetEvacuationInformationResponse(GetEvacuationInformationResponse getEvacuationInformationResponse) {
        this.getEvacuationInformationResponse = getEvacuationInformationResponse;
    }

    @Element(name = "GetEvacuationResponse", required = false)
    private GetEvacuationInformationResponse getEvacuationInformationResponse;




}
