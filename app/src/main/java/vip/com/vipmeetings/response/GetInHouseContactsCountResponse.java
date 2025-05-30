package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "GetInHouseContactsCountResponse", strict = false)
//@Namespace(reference = "http://tempuri.org/")
public class GetInHouseContactsCountResponse {


    public String getGetInHouseContactsCount() {
        return getInHouseContactsCount;
    }

    public void setGetInHouseContactsCount(String getInHouseContactsCount) {
        this.getInHouseContactsCount = getInHouseContactsCount;
    }

    @Element(name = "GetInHouseContactsCountResult", required = false)
    private String getInHouseContactsCount;



}
