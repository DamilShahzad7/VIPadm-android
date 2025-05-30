package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.RemoveClearTimeToEvacuation;
import vip.com.vipmeetings.request.SetClearTimeToEvacuations;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class RemoveClearTimeRequestBody {


    @Element(name = "RemoveClearTimeToEvacuation", required = false)
    private RemoveClearTimeToEvacuation setClearTimeToEvacuations;

    public RemoveClearTimeToEvacuation getSetClearTimeToEvacuations() {
        return setClearTimeToEvacuations;
    }

    public void setSetClearTimeToEvacuations(RemoveClearTimeToEvacuation setClearTimeToEvacuations) {
        this.setClearTimeToEvacuations = setClearTimeToEvacuations;
    }
}
