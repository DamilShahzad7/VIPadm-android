package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.models.GetExcelData;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetExcelDataRequestBody {


    @Element(name = "GetExcelData", required = false)
    private GetExcelData getExcelData;


    public GetExcelData getGetExcelData() {
        return getExcelData;
    }

    public void setGetExcelData(GetExcelData getExcelData) {
        this.getExcelData = getExcelData;
    }
}
