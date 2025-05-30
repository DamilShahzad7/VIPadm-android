package vip.com.vipmeetings.models;


import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(strict = false)
public class Products {

    @ElementList(entry = "Product", inline = true,required = false)
    private List<Product> Product;


    public List<vip.com.vipmeetings.models.Product> getProduct() {
        return Product;
    }

    public void setProduct(List<vip.com.vipmeetings.models.Product> product) {
        Product = product;
    }
}

