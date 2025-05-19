public class Shipment {
    private String productName;
    private int quantity;
    private String shipDate;
    private String status;

    public Shipment(String productName, int quantity, String shipDate, String status) {
        this.productName = productName;
        this.quantity = quantity;
        this.shipDate = shipDate;
        this.status = status;
    }

    // Getters
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public String getShipDate() { return shipDate; }
    public String getStatus() { return status; }
}