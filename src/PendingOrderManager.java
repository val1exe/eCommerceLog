import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

import java.util.List;
import java.util.Scanner;

public class PendingOrderManager {
    private Scanner scanner = new Scanner(System.in);

    public void handlePendingOrders() {
        System.out.println("\n===== PENDING ORDERS =====");

        // Load pending orders from file
        List<Order> allPendingOrders = PendingOrderFileManager.loadPendingOrders();

        // Filter orders for current seller
        String currentSeller = SellerSession.getSellerName();
        List<Order> sellerPendingOrders = filterOrdersBySeller(allPendingOrders, currentSeller);

        if (sellerPendingOrders.isEmpty()) {
            System.out.println("No pending orders found for your products.");
            return;
        }

        // Display pending orders
        displayOrders(sellerPendingOrders);

        // Option to update order status
        System.out.print("\nEnter order number to update status (0 to exit): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice > 0 && choice <= sellerPendingOrders.size()) {
            updateOrderStatus(sellerPendingOrders.get(choice - 1));
        }
    }

    private List<Order> filterOrdersBySeller(List<Order> orders, String sellerName) {
        List<Order> filteredOrders = new ArrayList<>();
        for (Order order : orders) {
            for (OrderItem item : order.getItems()) {
                if (item.getSeller().equals(sellerName)) {
                    filteredOrders.add(order);
                    break;
                }
            }
        }
        return filteredOrders;
    }

    private void displayOrders(List<Order> orders) {
        System.out.println("\n--------------------------------------------------");
        System.out.printf("%-4s %-12s %-15s %-10s %-10s\n",
                "No.", "Order ID", "Buyer", "Total", "Status");
        System.out.println("--------------------------------------------------");

        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            System.out.printf("%-4d %-12s %-15s ₱%-9.2f %-10s\n",
                    (i + 1),
                    order.getOrderId(),
                    order.getBuyerName(),
                    order.getTotalAmount(),
                    order.getStatus());
        }

        System.out.println("\nOrder Details:");
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            System.out.println("\n" + (i + 1) + ". " + order.getOrderId() + " - " + order.getBuyerName());
            for (OrderItem item : order.getItems()) {
                if (item.getSeller().equals(SellerSession.getSellerName())) {
                    System.out.println("   - " + item.getQuantity() + "x " + item.getProductName() +
                            " @ ₱" + item.getUnitPrice());
                }
            }
        }
    }

    private void updateOrderStatus(Order order) {
        System.out.println("\n===== UPDATE ORDER STATUS =====");
        System.out.println("Current status: " + order.getStatus());
        System.out.println("1. Mark as Processing");
        System.out.println("2. Mark as Shipped");
        System.out.println("3. Mark as Delivered");
        System.out.println("4. Cancel Order");
        System.out.print("Select new status: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                order.setStatus("Processing");
                break;
            case 2:
                order.setStatus("Shipped");
                break;
            case 3:
                order.setStatus("Delivered");
                break;
            case 4:
                order.setStatus("Cancelled");
                break;
            default:
                System.out.println("Invalid choice. Status not updated.");
                return;
        }

        // Update the order in pending orders file
        PendingOrderFileManager.removePendingOrder(order.getOrderId());

        // If order is not cancelled, move it to main orders file
        if (!order.getStatus().equals("Cancelled")) {
            List<Order> mainOrders = OrderFileManager.loadOrders();
            mainOrders.add(order);
            OrderFileManager.saveOrders(mainOrders);
        }

        System.out.println("Order status updated successfully!");
    }
}