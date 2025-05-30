package vip.com.vipmeetings.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.models.Empty;
import vip.com.vipmeetings.models.Evacuations;
import vip.com.vipmeetings.models.EvacuationsInformation;

@Root(name = "GetEvacuationResult", strict = false)
public class GetEvacuationResult {

    @Element(name = "Error", required = false)
    private vip.com.vipmeetings.models.Error Error;

    @Element(name = "Empty", required = false)
    private Empty empty;


    public EvacuationsInformation getEvacuationsInformation() {
        return evacuationsInformation;
    }

    public void setEvacuationsInformation(EvacuationsInformation evacuationsInformation) {
        this.evacuationsInformation = evacuationsInformation;
    }

    @Element(name = "Evacuations", required = false)
    private EvacuationsInformation evacuationsInformation;


    public vip.com.vipmeetings.models.Error getError() {
        return Error;
    }

    public void setError(vip.com.vipmeetings.models.Error error) {
        Error = error;
    }

    public Empty getEmpty() {
        return empty;
    }

    public void setEmpty(Empty empty) {
        this.empty = empty;
    }
}
