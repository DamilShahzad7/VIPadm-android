package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.AddContactLeave;
import vip.com.vipmeetings.request.StartEvacuation;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class StartEvacuationRequestBody {


    @Element(name = "StartEvacuation", required = false)
    private StartEvacuation startEvacuation;

    public StartEvacuation getStartEvacuation() {
        return startEvacuation;
    }

    public void setStartEvacuation(StartEvacuation startEvacuation) {
        this.startEvacuation = startEvacuation;
    }
}
