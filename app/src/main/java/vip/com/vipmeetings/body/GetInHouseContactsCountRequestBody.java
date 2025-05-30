package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.GetInHouseContactsCountRequest;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetInHouseContactsCountRequestBody {


    public GetInHouseContactsCountRequest getInHouseContactsCountRequest() {
        return getInHouseContactsCountRequest;
    }

    public void setInHouseContactsCountRequest(GetInHouseContactsCountRequest getInHouseContactsCountRequest) {
        this.getInHouseContactsCountRequest = getInHouseContactsCountRequest;
    }

    @Element(name = "GetInHouseContactsCount", required = false)
    private GetInHouseContactsCountRequest getInHouseContactsCountRequest;


}
