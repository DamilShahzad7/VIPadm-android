package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.GetEvacuationResponsibles;
import vip.com.vipmeetings.request.GetEvacuations;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetEvacuationsRequestBody {


    @Element(name = "GetEvacuations", required = false)
    private GetEvacuations getEvacuations;

    public GetEvacuations getGetEvacuations() {
        return getEvacuations;
    }

    public void setGetEvacuations(GetEvacuations getEvacuations) {
        this.getEvacuations = getEvacuations;
    }
}
