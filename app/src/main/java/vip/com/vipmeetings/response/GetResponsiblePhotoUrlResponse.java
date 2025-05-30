package vip.com.vipmeetings.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "GetResponsiblePhotoUrlResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetResponsiblePhotoUrlResponse {


    @Element(name = "GetResponsiblePhotoUrlResult", required = false)
    private String endEvacuationResult;

    public String getEndEvacuationResult() {
        return endEvacuationResult;
    }

    public void setEndEvacuationResult(String endEvacuationResult) {
        this.endEvacuationResult = endEvacuationResult;
    }

    //    @Element(name = "GetResponsiblePhotoUrlResult", required = false)
//    private GetResponsiblePhotoUrlResult getResponsiblePhotoUrlResult;
//
//    public GetResponsiblePhotoUrlResult getGetResponsiblePhotoUrlResult() {
//        return getResponsiblePhotoUrlResult;
//    }
//
//    public void setGetResponsiblePhotoUrlResult(GetResponsiblePhotoUrlResult getResponsiblePhotoUrlResult) {
//        this.getResponsiblePhotoUrlResult = getResponsiblePhotoUrlResult;
//    }
}
