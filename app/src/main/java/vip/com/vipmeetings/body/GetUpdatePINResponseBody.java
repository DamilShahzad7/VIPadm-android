package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.GetEvacuationInformationResponse;
import vip.com.vipmeetings.response.GetUpdatePINResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetUpdatePINResponseBody {

    public GetUpdatePINResponse getGetUpdatePINResponse() {
        return getUpdatePINResponse;
    }

    public void setGetUpdatePINResponse(GetUpdatePINResponse getUpdatePINResponse) {
        this.getUpdatePINResponse = getUpdatePINResponse;
    }

    @Element(name = "UpdateContactPinResponse", required = false)
    private GetUpdatePINResponse getUpdatePINResponse;




}
