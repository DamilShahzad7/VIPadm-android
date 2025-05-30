package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "GetRoomsResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetRoomsResponse {


    @Element(name = "GetRoomsResult", required = false)
    private GetRoomsResult getRoomsResult;

    public GetRoomsResult getGetRoomsResult() {
        return getRoomsResult;
    }

    public void setGetRoomsResult(GetRoomsResult getRoomsResult) {
        this.getRoomsResult = getRoomsResult;
    }
}
