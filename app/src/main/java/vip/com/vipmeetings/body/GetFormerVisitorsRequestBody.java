package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.GetFormerVisitors;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetFormerVisitorsRequestBody {


    @Element(name = "GetFormerVisitors", required = false)
    private GetFormerVisitors getFormerVisitors;


    public GetFormerVisitors getGetFormerVisitors() {
        return getFormerVisitors;
    }

    public void setGetFormerVisitors(GetFormerVisitors getFormerVisitors) {
        this.getFormerVisitors = getFormerVisitors;
    }
}
