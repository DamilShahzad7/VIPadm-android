package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.GetUpdatePINRequest;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetUpdatePINRequestBody {


    public GetUpdatePINRequest getGetUpdatePINRequest() {
        return getUpdatePINRequest;
    }

    public void setGetUpdatePINRequest(GetUpdatePINRequest getUpdatePINRequest) {
        this.getUpdatePINRequest = getUpdatePINRequest;
    }

    @Element(name = "UpdateContactPin", required = false)
    private GetUpdatePINRequest getUpdatePINRequest;


}
