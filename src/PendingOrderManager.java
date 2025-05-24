import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;




public class PendingOrderManager {
    private final Scanner scanner;  // Made final
    private final ShippingManager shippingManager;  // Made final

    public PendingOrderManager(ShippingManager shippingManager) {
        this.scanner = new Scanner(System.in);
        this.shippingManager = shippingManager;
    }
    public void handlePendingOrders() {
        System.out.println("\n===== PENDING ORDERS =====");

        // Load only pending orders for current seller
        List<Order> allOrders = OrderFileManager.loadOrdersForSeller(SellerSession.getSellerName());
        List<Order> pendingOrders = new ArrayList<>();

        for (Order order : allOrders) {
            if (order.getStatus().equals("Pending")) {
                pendingOrders.add(order);
            }
        }

        if (pendingOrders.isEmpty()) {
            System.out.println("No pending orders found.");
            return;
        }

        // Display orders
        for (int i = 0; i < pendingOrders.size(); i++) {
            Order order = pendingOrders.get(i);
            System.out.println((i+1) + ". Order #" + order.getOrderId());
            for (OrderItem item : order.getItems()) {
                if (item.getSeller().equals(SellerSession.getSellerName())) {
                    System.out.println("   - " + item.getQuantity() + "x " + item.getProductName());
                }
            }
        }

        System.out.print("\nEnter order number to process (0 to cancel): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice > 0 && choice <= pendingOrders.size()) {
            shippingManager.updateOrderStatus(pendingOrders.get(choice - 1));
        }
    }
    }