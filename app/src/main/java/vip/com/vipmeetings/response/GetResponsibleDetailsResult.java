package vip.com.vipmeetings.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.models.Employee;
import vip.com.vipmeetings.models.Empty;
import vip.com.vipmeetings.models.EvacuationResponsibles;

@Root(name = "GetResponsibleDetailsResult", strict = false)
public class GetResponsibleDetailsResult {



    @Element(name = "Error", required = false)
    private vip.com.vipmeetings.models.Error Error;

    @Element(name = "Empty", required = false)
    private Empty empty;



    @Element(name = "EvacuationResponsibles", required = false)
    private EvacuationResponsibles evacuationResponsibles;

    public EvacuationResponsibles getEvacuationResponsibles() {
        return evacuationResponsibles;
    }

    public void setEvacuationResponsibles(EvacuationResponsibles evacuationResponsibles) {
        this.evacuationResponsibles = evacuationResponsibles;
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
