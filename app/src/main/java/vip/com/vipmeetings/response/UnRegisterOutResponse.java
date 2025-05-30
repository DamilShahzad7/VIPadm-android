package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "EvacuateContactsResponse", strict = false)
//@Namespace(reference = "http://tempuri.org/")
public class UnRegisterOutResponse {


    public UnRegisterOutResult getUnRegisterOutResult() {
        return unregisterOutResult;
    }

    public void setUnRegisterOutResult(UnRegisterOutResult unregisterOutResult) {
        this.unregisterOutResult = unregisterOutResult;
    }

    @Element(name = "EvacuateContactsResult", required = false)
    private UnRegisterOutResult unregisterOutResult;



}
