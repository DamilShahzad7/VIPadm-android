package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.AddContactLeave;
import vip.com.vipmeetings.request.GetProducts;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetProductsRequestBody {


    @Element(name = "GetProducts", required = false)
    private GetProducts getProducts;

    public GetProducts getGetProducts() {
        return getProducts;
    }

    public void setGetProducts(GetProducts getProducts) {
        this.getProducts = getProducts;
    }
}
