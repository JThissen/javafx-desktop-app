package CustomDashboard;

public class TableData
{
    private String item;
    private String customerName;
    private double price;
    private String productID;

    public TableData(String item, String customerName, double price, String productID)
    {
        this.item = item;
        this.customerName = customerName;
        this.price = price;
        this.productID = productID;
    }

    public String getItem()
    {
        return item;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public double getPrice()
    {
        return price;
    }

    public String getProductID()
    {
        return productID;
    }

    public void setItem(String item)
    {
        this.item = item;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public void setProductID(String product)
    {
        this.productID = product;
    }
}