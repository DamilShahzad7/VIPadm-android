package vip.com.vipmeetings.models;


import org.simpleframework.xml.Root;

@Root(strict = false)
public class Product {
    private String Category;

    private String Description;

    private String CanteenId;

    private String CanteenName;

    private String CategoryId;

    private String Quantity;

    private String Price;

    private String Image1;

    private String OrderOpen;

    private String Currency;

    private String ProductId;

    public String getCategory() {
        return Category;
    }

    public void setCategory(String Category) {
        this.Category = Category;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getCanteenId() {
        return CanteenId;
    }

    public void setCanteenId(String CanteenId) {
        this.CanteenId = CanteenId;
    }

    public String getCanteenName() {
        return CanteenName;
    }

    public void setCanteenName(String CanteenName) {
        this.CanteenName = CanteenName;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String CategoryId) {
        this.CategoryId = CategoryId;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String Quantity) {
        this.Quantity = Quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String Price) {
        this.Price = Price;
    }

    public String getImage1() {
        return Image1;
    }

    public void setImage1(String Image1) {
        this.Image1 = Image1;
    }

    public String getOrderOpen() {
        return OrderOpen;
    }

    public void setOrderOpen(String OrderOpen) {
        this.OrderOpen = OrderOpen;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String Currency) {
        this.Currency = Currency;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String ProductId) {
        this.ProductId = ProductId;
    }

}