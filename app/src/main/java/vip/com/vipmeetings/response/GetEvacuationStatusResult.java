package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.models.Empty;
import vip.com.vipmeetings.models.Error;
import vip.com.vipmeetings.models.Evacuation;
import vip.com.vipmeetings.models.EvacuationStatus;
import vip.com.vipmeetings.models.GetEvacuationStatusModel;
import vip.com.vipmeetings.request.GetEvacuationStatus;

@Root(name = "GetEvacuationStatusResult", strict = false)
public class GetEvacuationStatusResult {


    @Element(name = "Error", required = false)
    private vip.com.vipmeetings.models.Error Error;

    @Element(name = "Empty", required = false)
    private Empty empty;


    @Element(name = "Evacuation",required = false)
    GetEvacuationStatusModel evacuation;

    public Empty getEmpty() {
        return empty;
    }

    public void setEmpty(Empty empty) {
        this.empty = empty;
    }

    public GetEvacuationStatusModel getEvacuation() {
        return evacuation;
    }

    public void setEvacuation(GetEvacuationStatusModel evacuation) {
        this.evacuation = evacuation;
    }

    public vip.com.vipmeetings.models.Error getError() {
        return Error;
    }

    public void setError(vip.com.vipmeetings.models.Error error) {
        Error = error;
    }
}
