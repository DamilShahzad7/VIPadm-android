package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "GetResponsibleDetailsResponse", strict = false)
public class GetResponsibleDetailsResponse {


    @Element(name = "GetResponsibleDetailsResult", required = false)
    private GetResponsibleDetailsResult getResponsibleDetailsResult;


    public GetResponsibleDetailsResult getGetResponsibleDetailsResult() {
        return getResponsibleDetailsResult;
    }

    public void setGetResponsibleDetailsResult(GetResponsibleDetailsResult getResponsibleDetailsResult) {
        this.getResponsibleDetailsResult = getResponsibleDetailsResult;
    }
}
