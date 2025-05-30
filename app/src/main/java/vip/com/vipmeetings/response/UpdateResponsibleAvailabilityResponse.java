package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "UpdateResponsibleAvailabilityResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class UpdateResponsibleAvailabilityResponse {


    @Element(name = "UpdateResponsibleAvailabilityResult", required = false)
    private String upDateResponsible;

    public String getUpDateResponsible() {
        return upDateResponsible;
    }

    public void setUpDateResponsible(String upDateResponsible) {
        this.upDateResponsible = upDateResponsible;
    }
}
