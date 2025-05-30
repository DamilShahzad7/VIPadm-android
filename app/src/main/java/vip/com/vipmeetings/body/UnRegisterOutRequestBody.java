package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.RegisterOutRequest;
import vip.com.vipmeetings.request.UnRegisterOutRequest;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class UnRegisterOutRequestBody {


    public UnRegisterOutRequest getUnRegisterOutRequest() {
        return unregisterOutRequest;
    }

    public void setUnRegisterOutRequest(UnRegisterOutRequest unregisterOutRequest) {
        this.unregisterOutRequest = unregisterOutRequest;
    }

    @Element(name = "EvacuateContacts", required = false)
    private UnRegisterOutRequest unregisterOutRequest;


}
