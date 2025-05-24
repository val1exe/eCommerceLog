import java.util.ArrayList;
import java.util.List;

public class SalesManager {
    private List<SaleRecord> salesRecords = new ArrayList<>();

    private static class SaleRecord {
        private String productName;
        private double totalRevenue;
        private int totalQuantitySold;

        public SaleRecord(String productName) {
            this.productName = productName;
            this.totalRevenue = 0;
            this.totalQuantitySold = 0;
        }

        public void addSale(int quantity, double price) {
            this.totalQuantitySold += quantity;
            this.totalRevenue += quantity * price;
        }

        // Getters
        public String getProductName() { return productName; }
        public double getTotalRevenue() { return totalRevenue; }
        public int getTotalQuantitySold() { return totalQuantitySold; }
    }

    public SalesManager() {
        // Initialize sales records for all seller's products
        String currentSeller = SellerSession.getSellerName();
        List<Product> sellerProducts = FileManager.loadProductsForSeller(currentSeller);

        for (Product product : sellerProducts) {
            salesRecords.add(new SaleRecord(product.getName()));
        }
    }

    public void handleDisplayAllSales() {
        System.out.println("\n===== SALES REPORT =====");

        // Load all delivered orders for this seller
        List<Order> sellerOrders = OrderFileManager.loadOrdersForSeller(SellerSession.getSellerName());
        List<Order> deliveredOrders = new ArrayList<>();

        for (Order order : sellerOrders) {
            if (order.getStatus().equals("Delivered")) {
                deliveredOrders.add(order);
            }
        }

        if (deliveredOrders.isEmpty()) {
            System.out.println("No delivered orders found.");
            return;
        }

        // Reset sales records
        salesRecords.clear();
        String currentSeller = SellerSession.getSellerName();
        List<Product> sellerProducts = FileManager.loadProductsForSeller(currentSeller);
        for (Product product : sellerProducts) {
            salesRecords.add(new SaleRecord(product.getName()));
        }

        // Process all delivered orders
        for (Order order : deliveredOrders) {
            for (OrderItem item : order.getItems()) {
                if (item.getSeller().equals(SellerSession.getSellerName())) {
                    // Find the product to get current price (in case price changed)
                    double currentPrice = getProductPrice(item.getProductName());
                    if (currentPrice > 0) {
                        addSale(item.getProductName(), item.getQuantity(), currentPrice);
                    }
                }
            }
        }

        // Display sales report
        System.out.println("\n--------------------------------------------------");
        System.out.printf("%-20s %-15s %-15s\n", "Product", "Quantity Sold", "Total Revenue");
        System.out.println("--------------------------------------------------");

        double grandTotal = 0;
        for (SaleRecord record : salesRecords) {
            if (record.getTotalQuantitySold() > 0) {
                System.out.printf("%-20s %-15d ₱%-15.2f\n",
                        record.getProductName(),
                        record.getTotalQuantitySold(),
                        record.getTotalRevenue());
                grandTotal += record.getTotalRevenue();
            }
        }

        System.out.println("--------------------------------------------------");
        System.out.printf("%-20s %-15s ₱%-15.2f\n", "GRAND TOTAL", "", grandTotal);
        System.out.println("--------------------------------------------------");
    }

    private void addSale(String productName, int quantity, double price) {
        for (SaleRecord record : salesRecords) {
            if (record.getProductName().equals(productName)) {
                record.addSale(quantity, price);
                return;
            }
        }
        // If product not found in records, add it
        SaleRecord newRecord = new SaleRecord(productName);
        newRecord.addSale(quantity, price);
        salesRecords.add(newRecord);
    }

    private double getProductPrice(String productName) {
        List<Product> products = FileManager.loadProductsForSeller(SellerSession.getSellerName());
        for (Product product : products) {
            if (product.getName().equals(productName)) {
                return product.getPrice();
            }
        }
        return 0;
    }
}