import java.io.*;
import java.util.*;

public class FileManager {
    private static final String PRODUCTS_FILE = "products.txt";

    // Save all products to file
    public static void saveProducts(List<Product> products) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PRODUCTS_FILE))) {
            for (Product product : products) {
                writer.println(product.getName() + "|" +
                        product.getPrice() + "|" +
                        product.getQuantity() + "|" +
                        product.getArrivalDate() + "|" +
                        product.getSeller() + "|" +
                        product.getLocation() + "|" +
                        product.getDescription() + "|" +
                        product.getCategory() + "|" +
                        product.isPerishable() + "|" +
                        product.getDeliveryType());
            }
            System.out.println("Products saved to file successfully.");
        } catch (IOException e) {
            System.out.println("Error saving products to file: " + e.getMessage());
        }
    }

    // Load all products from file
    public static List<Product> loadProducts() {
        List<Product> products = new ArrayList<>();
        File file = new File(PRODUCTS_FILE);

        if (!file.exists()) {
            System.out.println("No product file found. Starting with empty product list.");
            return products;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 10) {
                    String name = parts[0];
                    double price = Double.parseDouble(parts[1]);
                    int quantity = Integer.parseInt(parts[2]);
                    String arrivalDate = parts[3];
                    String seller = parts[4];
                    String location = parts[5];
                    String description = parts[6];
                    String category = parts[7];
                    boolean isPerishable = Boolean.parseBoolean(parts[8]);
                    String deliveryType = parts[9];

                    Product product = new Product(name, price, quantity, arrivalDate, seller, location);
                    product.setDescription(description);
                    product.setCategory(category);
                    product.setPerishable(isPerishable);
                    product.setDeliveryType(deliveryType);

                    products.add(product);
                }
            }
            System.out.println("Products loaded from file successfully.");
        } catch (IOException e) {
            System.out.println("Error loading products from file: " + e.getMessage());
        }

        return products;
    }


    public static List<Product> loadProductsForSeller(String sellerName) {
        List<Product> allProducts = loadProducts();
        List<Product> sellerProducts = new ArrayList<>();

        for (Product product : allProducts) {
            if (product.getSeller().equals(sellerName)) {
                sellerProducts.add(product);
            }
        }

        return sellerProducts;
    }
}
