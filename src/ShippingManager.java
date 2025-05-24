import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ShippingManager {
    private Scanner scanner = new Scanner(System.in);

    public void handleProductShipping() {
        System.out.println("\n===== PRODUCT SHIPPING =====");

        // Load all orders for current seller
        List<Order> sellerOrders = OrderFileManager.loadOrdersForSeller(SellerSession.getSellerName());

        // Filter orders that can be shipped/delivered
        List<Order> processableOrders = new ArrayList<>();
        for (Order order : sellerOrders) {
            if (order.getStatus().equals("Pending") || order.getStatus().equals("Shipped")) {
                processableOrders.add(order);
            }
        }

        if (processableOrders.isEmpty()) {
            System.out.println("No orders available for shipping/delivery.");
            return;
        }

        // Display orders
        System.out.println("No.\tOrder ID\t\t\tStatus\t\tProducts");
        for (int i = 0; i < processableOrders.size(); i++) {
            Order order = processableOrders.get(i);
            System.out.print((i+1) + ".\t" + order.getOrderId() + "\t" + order.getStatus() + "\t\t");
            for (OrderItem item : order.getItems()) {
                if (item.getSeller().equals(SellerSession.getSellerName())) {
                    System.out.print(item.getProductName() + " (" + item.getQuantity() + "), ");
                }
            }
            System.out.println();
        }

        // Get user input
        System.out.print("\nEnter order number to update status (0 to cancel): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice > 0 && choice <= processableOrders.size()) {
            updateOrderStatus(processableOrders.get(choice - 1));
        }
    }

    public void updateOrderStatus(Order order) {
        System.out.println("\n===== UPDATE STATUS =====");
        System.out.println("Current status: " + order.getStatus());
        System.out.println("1. Mark as Shipped");
        System.out.println("2. Mark as Delivered");
        System.out.println("3. Cancel Order");
        System.out.print("Select action: ");

        int action = scanner.nextInt();
        scanner.nextLine();

        switch (action) {
            case 1:
                order.setStatus("Shipped");
                break;
            case 2:
                order.setStatus("Delivered");
                break;
            case 3:
                order.setStatus("Cancelled");
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        // Update order in the main orders file
        List<Order> allOrders = OrderFileManager.loadOrders();
        allOrders.removeIf(o -> o.getOrderId().equals(order.getOrderId()));
        allOrders.add(order);
        OrderFileManager.saveOrders(allOrders);

        System.out.println("Order status updated to: " + order.getStatus());
    }
}