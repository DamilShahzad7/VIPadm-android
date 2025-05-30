package vip.com.vipmeetings.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "GetInHouseContactsCountResult", strict = false)
public class GetInHouseContactsCountResult {

    @Element(name = "Error", required = false)
    private String Error;
    @Element(name = "GetInHouseContactsCountResult", required = false)
    private String count;
    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
