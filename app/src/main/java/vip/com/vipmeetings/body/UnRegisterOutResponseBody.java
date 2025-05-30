package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.RegisterOutResponse;
import vip.com.vipmeetings.response.UnRegisterOutResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class UnRegisterOutResponseBody {


    public UnRegisterOutResponse getUnRegisterOutResponse() {
        return unregisterOutResponse;
    }

    public void setUnRegisterOutResponse(UnRegisterOutResponse unregisterOutResponse) {
        this.unregisterOutResponse = unregisterOutResponse;
    }

    @Element(name = "EvacuateContactsResponse", required = false)
    private UnRegisterOutResponse unregisterOutResponse;




}
