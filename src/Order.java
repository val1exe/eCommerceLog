import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Order {
    private String orderId;
    private String orderDate;
    private String status;
    private String buyerName;
    private String deliveryAddress;
    private String paymentMethod;
    private double totalAmount;
    private List<OrderItem> items;

    public Order(String orderId, String orderDate, String status, String buyerName,
                 String deliveryAddress, String paymentMethod, double totalAmount, List<OrderItem> items) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.status = status;
        this.buyerName = buyerName;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount;
        this.items = items;
    }

    // Constructor for creating a new order
    public Order(String buyerName, String deliveryAddress, String paymentMethod, List<OrderItem> items) {
        this.orderId = generateOrderId();
        this.orderDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.status = "Pending";
        this.buyerName = buyerName;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
        this.items = new ArrayList<>(items);

        // Calculate total amount
        this.totalAmount = 0;
        for (OrderItem item : items) {
            this.totalAmount += item.getUnitPrice() * item.getQuantity();
        }
    }

    private String generateOrderId() {
        // Generate a random order ID
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Getters
    public String getOrderId() { return orderId; }
    public String getOrderDate() { return orderDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getBuyerName() { return buyerName; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public String getPaymentMethod() { return paymentMethod; }
    public double getTotalAmount() { return totalAmount; }
    public List<OrderItem> getItems() { return items; }
}