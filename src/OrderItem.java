public class OrderItem {
    private String productName;
    private int quantity;
    private double unitPrice;
    private String seller;
    private String deliveryType;
    private boolean isPerishable;

    public OrderItem(String productName, int quantity, double unitPrice, String seller,
                     String deliveryType, boolean isPerishable) {
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.seller = seller;
        this.deliveryType = deliveryType;
        this.isPerishable = isPerishable;
    }


    // Getters and setters
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public String getSeller() { return seller; }
    public void setSeller(String seller) { this.seller = seller; }

    public String getDeliveryType() { return deliveryType; }
    public boolean isPerishable() { return isPerishable; }

    // Calculate total price for this item
    public double getTotalPrice() {
        return quantity * unitPrice;
    }
}