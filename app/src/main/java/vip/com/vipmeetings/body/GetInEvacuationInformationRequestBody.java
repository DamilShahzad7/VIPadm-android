package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.GetEvacuationInformationRequest;
import vip.com.vipmeetings.request.GetInHouseContactsCountRequest;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetInEvacuationInformationRequestBody {


    public GetEvacuationInformationRequest getGetEvacuationInformationRequest() {
        return getEvacuationInformationRequest;
    }

    public void setGetEvacuationInformationRequest(GetEvacuationInformationRequest getEvacuationInformationRequest) {
        this.getEvacuationInformationRequest = getEvacuationInformationRequest;
    }

    @Element(name = "GetEvacuation", required = false)
    private GetEvacuationInformationRequest getEvacuationInformationRequest;


}
