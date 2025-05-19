import java.util.ArrayList;
import java.util.List;

public class SalesManager {
    private List<ProductSales> salesRecords = new ArrayList<>();

    // Class to store product name and its sales
    private static class ProductSales {
        private String productName;
        private List<Sale> sales;

        public ProductSales(String productName) {
            this.productName = productName;
            this.sales = new ArrayList<>();
        }

        public String getProductName() {
            return productName;
        }

        public List<Sale> getSales() {
            return sales;
        }
    }

    public SalesManager() {
        // Initialize sales records from product file
        List<Product> products = FileManager.loadProducts();
        for (Product product : products) {
            initializeProductSales(product.getName());
        }
    }

    public void handleDisplayAllSales(ProductManager productManager) {
        System.out.println("\n===== DISPLAY ALL SALES =====");

        if (productManager.getProducts().isEmpty()) {
            System.out.println("No products available. You need to add products first.");
            return;
        }

        System.out.print("Enter the name of a product or category: ");
        String productName = System.console().readLine();

        // Find the product in our list
        ProductSales productSales = findProductSales(productName);

        if (productSales != null) {
            System.out.println("\nSales for " + productName + ":");
            System.out.println("Date\t\tQuantity");
            System.out.println("----------------------");
            List<Sale> sales = productSales.getSales();
            for (Sale sale : sales) {
                System.out.println(sale.getDate() + "\t" + sale.getQuantity());
            }
        } else {
            System.out.println("\nProduct does not exist or has no sales records.");
        }
    }

    public void initializeProductSales(String productName) {
        // Check if product already exists to avoid duplication
        if (findProductSales(productName) == null) {
            salesRecords.add(new ProductSales(productName));
        }
    }

    public void removeProductSales(String productName) {
        for (int i = 0; i < salesRecords.size(); i++) {
            if (salesRecords.get(i).getProductName().equals(productName)) {
                salesRecords.remove(i);
                break;
            }
        }
    }

    // Helper method to find product sales by name
    private ProductSales findProductSales(String productName) {
        for (ProductSales ps : salesRecords) {
            if (ps.getProductName().equals(productName)) {
                return ps;
            }
        }
        return null;
    }
}