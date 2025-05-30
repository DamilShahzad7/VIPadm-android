package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.AddContactLeave;
import vip.com.vipmeetings.request.AddOrder;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class AddOrderRequestBody {


    @Element(name = "AddOrder", required = false)
    private AddOrder addOrder;

    public AddOrder getAddOrder() {
        return addOrder;
    }

    public void setAddOrder(AddOrder addOrder) {
        this.addOrder = addOrder;
    }
}
