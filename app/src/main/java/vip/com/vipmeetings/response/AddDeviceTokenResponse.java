package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "AddDeviceTokenResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class AddDeviceTokenResponse {





    @Element(name = "AddDeviceTokenResult", required = false)
    private AddDeviceTokenResult addDeviceTokenResult;

    public AddDeviceTokenResult getAddDeviceTokenResult() {
        return addDeviceTokenResult;
    }

    public void setAddDeviceTokenResult(AddDeviceTokenResult addDeviceTokenResult) {
        this.addDeviceTokenResult = addDeviceTokenResult;
    }
}
