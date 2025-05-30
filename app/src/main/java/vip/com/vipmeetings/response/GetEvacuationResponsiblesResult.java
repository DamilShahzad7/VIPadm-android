package vip.com.vipmeetings.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.models.Empty;
import vip.com.vipmeetings.models.EvacuationResponsibles;

@Root(name = "GetEvacuationResponsiblesResult", strict = false)
public class GetEvacuationResponsiblesResult {

    @Element(name = "Error", required = false)
    private vip.com.vipmeetings.models.Error Error;

    @Element(name = "Empty", required = false)
    private Empty empty;


    @Element(name = "EvacuationResponsibles", required = false)
    private EvacuationResponsibles getEvacuationResponsibles;

    public EvacuationResponsibles getGetEvacuationResponsibles() {
        return getEvacuationResponsibles;
    }

    public void setGetEvacuationResponsibles(EvacuationResponsibles getEvacuationResponsibles) {
        this.getEvacuationResponsibles = getEvacuationResponsibles;
    }

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
