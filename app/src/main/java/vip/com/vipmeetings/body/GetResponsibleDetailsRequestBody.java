package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.GetResponsibleDetails;
import vip.com.vipmeetings.request.ValidateUserRequest;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetResponsibleDetailsRequestBody {



    @Element(name = "GetResponsibleDetails", required = false)
    private GetResponsibleDetails getResponsibleDetails;


    public GetResponsibleDetails getGetResponsibleDetails() {
        return getResponsibleDetails;
    }

    public void setGetResponsibleDetails(GetResponsibleDetails getResponsibleDetails) {
        this.getResponsibleDetails = getResponsibleDetails;
    }
}
