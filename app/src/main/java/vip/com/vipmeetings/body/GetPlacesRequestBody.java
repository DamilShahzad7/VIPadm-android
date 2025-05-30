package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.models.GetPlaces;
import vip.com.vipmeetings.request.AddContactLeave;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetPlacesRequestBody {


    @Element(name = "GetPlaces", required = false)
    private GetPlaces getPlaces;

    public GetPlaces getGetPlaces() {
        return getPlaces;
    }

    public void setGetPlaces(GetPlaces getPlaces) {
        this.getPlaces = getPlaces;
    }
}
