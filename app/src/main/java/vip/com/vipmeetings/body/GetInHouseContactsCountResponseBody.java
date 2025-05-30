package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.GetInHouseContactsCountResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetInHouseContactsCountResponseBody {





    public GetInHouseContactsCountResponse getInHouseContactsCountResponse() {
        return getInHouseContactsCountResponse;
    }

    public void setInHouseContactsCountResponse(GetInHouseContactsCountResponse getInHouseContactsCountResponse) {
        this.getInHouseContactsCountResponse = getInHouseContactsCountResponse;
    }

    @Element(name = "GetInHouseContactsCountResponse", required = false)
    private GetInHouseContactsCountResponse getInHouseContactsCountResponse;




}
