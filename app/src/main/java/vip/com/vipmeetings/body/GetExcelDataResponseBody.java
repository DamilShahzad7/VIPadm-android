package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.GetExcelDataResponse;
import vip.com.vipmeetings.response.GetFormerVisitorsResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetExcelDataResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "GetExcelDataResponse", required = false)
    private GetExcelDataResponse getExcelDataResponse;

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }

    public GetExcelDataResponse getGetExcelDataResponse() {
        return getExcelDataResponse;
    }

    public void setGetExcelDataResponse(GetExcelDataResponse getExcelDataResponse) {
        this.getExcelDataResponse = getExcelDataResponse;
    }
}
