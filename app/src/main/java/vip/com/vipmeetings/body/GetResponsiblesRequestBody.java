package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.AddContactLeave;
import vip.com.vipmeetings.request.GetEvacuationResponsibles;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetResponsiblesRequestBody {


    @Element(name = "GetEvacuationResponsibles", required = false)
    private GetEvacuationResponsibles getEvacuationResponsibles;

    public GetEvacuationResponsibles getGetEvacuationResponsibles() {
        return getEvacuationResponsibles;
    }

    public void setGetEvacuationResponsibles(GetEvacuationResponsibles getEvacuationResponsibles) {
        this.getEvacuationResponsibles = getEvacuationResponsibles;
    }
}
