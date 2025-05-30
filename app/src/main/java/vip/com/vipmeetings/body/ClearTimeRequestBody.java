package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.EndEvacuation;
import vip.com.vipmeetings.request.SetClearTimeToEvacuations;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class ClearTimeRequestBody {


    @Element(name = "SetClearTimeToEvacuations", required = false)
    private SetClearTimeToEvacuations setClearTimeToEvacuations;

    public SetClearTimeToEvacuations getSetClearTimeToEvacuations() {
        return setClearTimeToEvacuations;
    }

    public void setSetClearTimeToEvacuations(SetClearTimeToEvacuations setClearTimeToEvacuations) {
        this.setClearTimeToEvacuations = setClearTimeToEvacuations;
    }
}
