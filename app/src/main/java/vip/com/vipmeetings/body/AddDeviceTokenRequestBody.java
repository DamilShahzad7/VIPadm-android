package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.AddDeviceToken;
import vip.com.vipmeetings.request.UpdateContactLeave;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class AddDeviceTokenRequestBody {


    @Element(name = "AddDeviceToken", required = false)
    private AddDeviceToken addDeviceToken;

    public AddDeviceToken getAddDeviceToken() {
        return addDeviceToken;
    }

    public void setAddDeviceToken(AddDeviceToken addDeviceToken) {
        this.addDeviceToken = addDeviceToken;
    }
}
