package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "AddOrderResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class AddOrderResponse {


    @Element(name = "AddOrderResult", required = false)
    private AddOrderResult addOrderResult;

    public AddOrderResult getAddOrderResult() {
        return addOrderResult;
    }

    public void setAddOrderResult(AddOrderResult addOrderResult) {
        this.addOrderResult = addOrderResult;
    }
}
