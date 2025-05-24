import java.io.*;
import java.util.*;

public class  OrderFileManager {
    private static final String ORDERS_FILE = "orders.txt";

    // Save all orders to file
    public static void saveOrders(List<Order> orders) {
        // Create a new list to avoid modifying the input list
        List<Order> uniqueOrders = new ArrayList<>();

        // Remove duplicates by order ID
        for (Order order : orders) {
            boolean exists = false;
            for (Order uniqueOrder : uniqueOrders) {
                if (uniqueOrder.getOrderId().equals(order.getOrderId())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                uniqueOrders.add(order);
            }
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(ORDERS_FILE))) {
            for (Order order : uniqueOrders) {
                StringBuilder itemsStr = new StringBuilder();
                for (OrderItem item : order.getItems()) {
                    itemsStr.append(item.getProductName()).append(",")
                            .append(item.getQuantity()).append(",")
                            .append(item.getUnitPrice()).append(",")
                            .append(item.getSeller()).append(";");
                }

                String itemsData = itemsStr.toString();
                if (itemsData.endsWith(";")) {
                    itemsData = itemsData.substring(0, itemsData.length() - 1);
                }

                writer.println(order.getOrderId() + "|" +
                        order.getOrderDate() + "|" +
                        order.getStatus() + "|" +
                        order.getBuyerName() + "|" +
                        order.getDeliveryAddress() + "|" +
                        order.getPaymentMethod() + "|" +
                        order.getTotalAmount() + "|" +
                        itemsData);
            }
            System.out.println("Orders saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving orders: " + e.getMessage());
        }
    }

    public static List<Order> loadOrdersForSeller(String sellerName) {
        List<Order> sellerOrders = new ArrayList<>();
        List<Order> allOrders = loadOrders();

        for (Order order : allOrders) {
            for (OrderItem item : order.getItems()) {
                if (item.getSeller().equals(sellerName)) {
                    sellerOrders.add(order);
                    break;  // Add order once even if it has multiple items from seller
                }
            }
        }
        return sellerOrders;
    }

    // Load all orders from file
    public static List<Order> loadOrders() {
        List<Order> orders = new ArrayList<>();
        File file = new File(ORDERS_FILE);

        if (!file.exists()) {
            System.out.println("No orders file found. Starting with empty orders list.");
            return orders;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ORDERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 7) {
                    String orderId = parts[0];
                    String orderDate = parts[1];
                    String status = parts[2];
                    String buyerName = parts[3];
                    String deliveryAddress = parts[4];
                    String paymentMethod = parts[5];
                    double totalAmount = Double.parseDouble(parts[6]);

                    // Parse order items
                    List<OrderItem> items = new ArrayList<>();
                    if (parts.length > 7) {
                        String[] itemsArray = parts[7].split(";");
                        for (String itemStr : itemsArray) {
                            String[] itemParts = itemStr.split(",");
                            if (itemParts.length >= 4) {
                                String productName = itemParts[0];
                                int quantity = Integer.parseInt(itemParts[1]);
                                double unitPrice = Double.parseDouble(itemParts[2]);
                                String seller = itemParts[3];

                                items.add(new OrderItem(productName, quantity, unitPrice, seller));
                            }
                        }
                    }

                    orders.add(new Order(orderId, orderDate, status, buyerName,
                            deliveryAddress, paymentMethod, totalAmount, items));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading orders from file: " + e.getMessage());
        }

        return orders;
    }
}