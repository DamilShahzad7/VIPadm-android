package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.GetContactLeaves;
import vip.com.vipmeetings.request.GetEvacuationStatus;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetEvacuationStatusRequestBody {


    @Element(name = "GetEvacuationStatus", required = false)
    private GetEvacuationStatus getEvacuationStatus;

    public GetEvacuationStatus getGetEvacuationStatus() {
        return getEvacuationStatus;
    }

    public void setGetEvacuationStatus(GetEvacuationStatus getEvacuationStatus) {
        this.getEvacuationStatus = getEvacuationStatus;
    }
}
