package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.AddDeviceToken;
import vip.com.vipmeetings.request.GetContacts;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetContactsRequestBody {


    @Element(name = "GetContacts", required = false)
    private GetContacts getContacts;

    public GetContacts getGetContacts() {
        return getContacts;
    }

    public void setGetContacts(GetContacts getContacts) {
        this.getContacts = getContacts;
    }
}
