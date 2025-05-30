package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "GetExcelDataResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetExcelDataResponse {

    @Element(name = "GetExcelDataResult", required = false)
    private GetExcelDataResult getExcelDataResult;


    public GetExcelDataResult getGetExcelDataResult() {
        return getExcelDataResult;
    }

    public void setGetExcelDataResult(GetExcelDataResult getExcelDataResult) {
        this.getExcelDataResult = getExcelDataResult;
    }
}
