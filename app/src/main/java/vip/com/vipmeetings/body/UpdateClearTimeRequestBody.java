package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.SetClearTimeToEvacuations;
import vip.com.vipmeetings.request.UpdateClearTimeToEvacuation;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class UpdateClearTimeRequestBody {


    @Element(name = "UpdateClearTimeToEvacuation", required = false)
    private UpdateClearTimeToEvacuation updateClearTimeToEvacuation;

    public UpdateClearTimeToEvacuation getUpdateClearTimeToEvacuation() {
        return updateClearTimeToEvacuation;
    }

    public void setUpdateClearTimeToEvacuation(UpdateClearTimeToEvacuation updateClearTimeToEvacuation) {
        this.updateClearTimeToEvacuation = updateClearTimeToEvacuation;
    }
}
