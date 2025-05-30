package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "GetProductsResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetProductsResponse {


    @Element(name = "GetProductsResult", required = false)
    private GetProductsResult getProductsResult;

    public GetProductsResult getGetProductsResult() {
        return getProductsResult;
    }

    public void setGetProductsResult(GetProductsResult getProductsResult) {
        this.getProductsResult = getProductsResult;
    }
}
