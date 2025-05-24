public class Product {
    private String name;
    private double price;
    private int quantity;
    private String arrivalDate;
    private String seller;
    private String location;
    private String description;
    private String category;
    private boolean isPerishable;


    public Product(String name, double price, int quantity, String arrivalDate) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.arrivalDate = arrivalDate;

        // Get seller info from the active session
        this.seller = SellerSession.getSellerName();
        this.location = SellerSession.getSellerLocation();

        this.description = "";
        this.category = "";
        this.isPerishable = false;

    }

    // Overloaded constructor for backward compatibility
    public Product(String name, double price, int quantity, String arrivalDate, String seller, String location) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.arrivalDate = arrivalDate;
        this.seller = seller;
        this.location = location;
        this.description = "";
        this.category = "";
        this.isPerishable = false;

    }


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getArrivalDate() { return arrivalDate; }
    public void setArrivalDate(String arrivalDate) { this.arrivalDate = arrivalDate; }

    public String getSeller() { return seller; }
    public void setSeller(String seller) { this.seller = seller; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isPerishable() { return isPerishable; }
    public void setPerishable(boolean perishable) { isPerishable = perishable; }

}