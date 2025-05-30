package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.AddContactLeave;
import vip.com.vipmeetings.request.EndEvacuation;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class EndEvacuationRequestBody {


    @Element(name = "EndEvacuation", required = false)
    private EndEvacuation endEvacuation;

    public EndEvacuation getEndEvacuation() {
        return endEvacuation;
    }

    public void setEndEvacuation(EndEvacuation endEvacuation) {
        this.endEvacuation = endEvacuation;
    }
}
