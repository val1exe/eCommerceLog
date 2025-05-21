import java.io.*;
import java.util.*;

public class PendingOrderFileManager {
    private static final String PENDING_ORDERS_FILE = "pending_orders.txt";

    // Save pending order to file
    public static void savePendingOrder(Order order) {
        // First, check if the order is already in the pending orders
        List<Order> pendingOrders = loadPendingOrders();
        boolean orderExists = false;

        for (Order existingOrder : pendingOrders) {
            if (existingOrder.getOrderId().equals(order.getOrderId())) {
                orderExists = true;
                break;
            }
        }


        if (!orderExists) {
            pendingOrders.add(order);
            // Write all pending orders back to file
            try (PrintWriter writer = new PrintWriter(new FileWriter(PENDING_ORDERS_FILE))) {
                for (Order pendingOrder : pendingOrders) {
                    StringBuilder itemsStr = new StringBuilder();
                    for (OrderItem item : pendingOrder.getItems()) {
                        // Format: productName,quantity,unitPrice,seller;
                        itemsStr.append(item.getProductName()).append(",")
                                .append(item.getQuantity()).append(",")
                                .append(item.getUnitPrice()).append(",")
                                .append(item.getSeller()).append(";");
                    }


                    String itemsData = itemsStr.toString();
                    if (itemsData.endsWith(";")) {
                        itemsData = itemsData.substring(0, itemsData.length() - 1);
                    }


                    writer.println(pendingOrder.getOrderId() + "|" +
                            pendingOrder.getOrderDate() + "|" +
                            pendingOrder.getStatus() + "|" +
                            pendingOrder.getBuyerName() + "|" +
                            pendingOrder.getDeliveryAddress() + "|" +
                            pendingOrder.getPaymentMethod() + "|" +
                            pendingOrder.getTotalAmount() + "|" +
                            itemsData);
                }
                System.out.println("Pending order saved successfully.");
            } catch (IOException e) {
                System.out.println("Error saving pending order: " + e.getMessage());
            }
        }
    }

    // Load all pending orders from file
    public static List<Order> loadPendingOrders() {
        List<Order> pendingOrders = new ArrayList<>();
        File file = new File(PENDING_ORDERS_FILE);

        if (!file.exists()) {
            System.out.println("No pending orders file found. Starting with empty pending orders list.");
            return pendingOrders;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(PENDING_ORDERS_FILE))) {
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

                    pendingOrders.add(new Order(orderId, orderDate, status, buyerName,
                            deliveryAddress, paymentMethod, totalAmount, items));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading pending orders from file: " + e.getMessage());
        }

        return pendingOrders;
    }


    public static void removePendingOrder(String orderId) {
        List<Order> pendingOrders = loadPendingOrders();
        pendingOrders.removeIf(order -> order.getOrderId().equals(orderId));

        try (PrintWriter writer = new PrintWriter(new FileWriter(PENDING_ORDERS_FILE))) {
            for (Order pendingOrder : pendingOrders) {
                StringBuilder itemsStr = new StringBuilder();
                for (OrderItem item : pendingOrder.getItems()) {
                    // Format: productName,quantity,unitPrice,seller;
                    itemsStr.append(item.getProductName()).append(",")
                            .append(item.getQuantity()).append(",")
                            .append(item.getUnitPrice()).append(",")
                            .append(item.getSeller()).append(";");
                }


                String itemsData = itemsStr.toString();
                if (itemsData.endsWith(";")) {
                    itemsData = itemsData.substring(0, itemsData.length() - 1);
                }


                writer.println(pendingOrder.getOrderId() + "|" +
                        pendingOrder.getOrderDate() + "|" +
                        pendingOrder.getStatus() + "|" +
                        pendingOrder.getBuyerName() + "|" +
                        pendingOrder.getDeliveryAddress() + "|" +
                        pendingOrder.getPaymentMethod() + "|" +
                        pendingOrder.getTotalAmount() + "|" +
                        itemsData);
            }
            System.out.println("Pending order removed successfully.");
        } catch (IOException e) {
            System.out.println("Error removing pending order: " + e.getMessage());
        }
    }
}