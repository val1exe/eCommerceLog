import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProductManager {
    private Scanner scanner = new Scanner(System.in);
    private List<Product> products = new ArrayList<>();

    public ProductManager() {
        // Load products from file when initializing
        products = FileManager.loadProductsForSeller(SellerSession.getSellerName());
    }

    public boolean handleManageProducts(StockManager stockManager, DashboardController dashboardController) {
        System.out.println("\n===== MANAGE PRODUCTS =====");
        while (true) {
            boolean shouldReturn = displayManageProductOptions(stockManager, dashboardController);
            if (shouldReturn) {
                return true; // Return to main dashboard
            }
            // Otherwise continue in manage products menu
        }
    }

    private boolean displayManageProductOptions(StockManager stockManager, DashboardController dashboardController) {
        System.out.println("\n===== MANAGE PRODUCTS MENU =====");
        System.out.println("1. Add product");
        System.out.println("2. Edit product");
        System.out.println("3. Delete product");
        System.out.println("4. Product Information");
        System.out.println("5. Go back to Dashboard");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                handleAddProduct(stockManager);
                return false;
            case 2:
                handleEditProduct(stockManager);
                return false;
            case 3:
                handleDeleteProduct(stockManager);
                return false;
            case 4:
                handleProductInformation();
                return false;
            case 5:
                return true;
            default:
                System.out.println("Invalid choice. Please try again.");
                return false;
        }
    }

    public void handleAddProduct(StockManager stockManager) {
        boolean addingProducts = true;

        while (addingProducts) {
            System.out.println("\n===== ADD PRODUCT =====");
            System.out.println("0. Exit without adding");

            System.out.print("Enter product name (or 0 to exit): ");
            String name = scanner.nextLine();

            if (name.equals("0")) {
                System.out.println("Add product cancelled.");
                break;
            }

            System.out.print("Enter product price (or 0 to exit): ");
            String priceInput = scanner.nextLine();
            if (priceInput.equals("0")) {
                System.out.println("Add product cancelled.");
                break;
            }
            double price = Double.parseDouble(priceInput);

            System.out.print("Enter product quantity (or 0 to exit): ");
            String quantityInput = scanner.nextLine();
            if (quantityInput.equals("0")) {
                System.out.println("Add product cancelled.");
                break;
            }
            int quantity = Integer.parseInt(quantityInput);

            System.out.print("Enter product description (or 0 to exit): ");
            String description = scanner.nextLine();
            if (description.equals("0")) {
                System.out.println("Add product cancelled.");
                break;
            }

            System.out.print("Enter product category (or 0 to exit): ");
            String category = scanner.nextLine();
            if (category.equals("0")) {
                System.out.println("Add product cancelled.");
                break;
            }

            System.out.print("Is product perishable? (Y/N/0 to exit): ");
            String perishableInput = scanner.nextLine().toUpperCase();
            if (perishableInput.equals("0")) {
                System.out.println("Add product cancelled.");
                break;
            }
            boolean isPerishable = perishableInput.equals("Y");


            // Get seller information from SellerSession
            String sellerName = SellerSession.getSellerName();
            String sellerLocation = SellerSession.getSellerLocation();

            System.out.println("\nAdding product with seller information:");
            System.out.println("Seller: " + sellerName);
            System.out.println("Location: " + sellerLocation);

            Product newProduct = new Product(name, price, quantity,
                    java.time.LocalDate.now().plusDays(7).toString(),
                    sellerName, sellerLocation);
            newProduct.setDescription(description);
            newProduct.setCategory(category);
            newProduct.setPerishable(isPerishable);


            addProduct(newProduct);
            stockManager.addStock(new Stock(name.substring(0, Math.min(4, name.length())).toUpperCase(),
                    name, price, quantity));

            // Save products to file after adding
            FileManager.saveProducts(products);

            System.out.println("Product added successfully!");

            System.out.print("\nDo you want to add another product? (Y/N): ");
            String addAnother = scanner.nextLine().toUpperCase();

            if (!addAnother.equals("Y")) {
                addingProducts = false;
            }
        }
    }

    public void handleEditProduct(StockManager stockManager) {
        boolean editing = true;

        while (editing) {
            System.out.println("\n===== EDIT PRODUCT =====");
            System.out.println("0. Exit without editing");
            displaySellerProducts();

            System.out.print("\nEnter the number of the product you want to edit (or 0 to exit): ");
            String input = scanner.nextLine();

            if (input.equals("0")) {
                System.out.println("Edit product cancelled.");
                break;
            }

            try {
                int productIndex = Integer.parseInt(input);

                if (productIndex < 0 || productIndex > products.size()) {
                    System.out.println("Invalid product number.");
                    continue;
                }

                Product selectedProduct = products.get(productIndex - 1);

                System.out.println("\nWhat would you like to edit?");
                System.out.println("0. Exit without editing");
                System.out.println("1. Name");
                System.out.println("2. Price");
                System.out.println("3. Quantity");
                System.out.println("4. Description");
                System.out.println("5. Category");
                System.out.print("Enter your choice (0-5): ");

                String editChoiceInput = scanner.nextLine();
                if (editChoiceInput.equals("0")) {
                    System.out.println("Edit product cancelled.");
                    break;
                }
                int editChoice = Integer.parseInt(editChoiceInput);

                if (editChoice < 1 || editChoice > 5) {
                    System.out.println("Invalid choice.");
                    continue;
                }

                System.out.print("Enter new value (or 0 to cancel): ");
                String newValue = scanner.nextLine();

                if (newValue.equals("0")) {
                    System.out.println("Edit cancelled.");
                    continue;
                }

                switch (editChoice) {
                    case 1:
                        selectedProduct.setName(newValue);
                        stockManager.updateStockName(selectedProduct.getName(), newValue);
                        break;
                    case 2:
                        selectedProduct.setPrice(Double.parseDouble(newValue));
                        stockManager.updateStockPrice(selectedProduct.getName(), Double.parseDouble(newValue));
                        break;
                    case 3:
                        selectedProduct.setQuantity(Integer.parseInt(newValue));
                        stockManager.updateStockQuantity(selectedProduct.getName(), Integer.parseInt(newValue));
                        break;
                    case 4:
                        selectedProduct.setDescription(newValue);
                        break;
                    case 5:
                        selectedProduct.setCategory(newValue);
                        break;
                }

                // Save products to file after editing
                FileManager.saveProducts(products);

                System.out.println("\nProduct successfully edited.");

                System.out.print("\nDo you want to edit another product? (Y/N): ");
                String editAnother = scanner.nextLine().toUpperCase();
                if (!editAnother.equals("Y")) {
                    editing = false;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    public void handleDeleteProduct(StockManager stockManager) {
        boolean deleting = true;

        while (deleting) {
            System.out.println("\n===== DELETE PRODUCT =====");
            System.out.println("0. Exit without deleting");
            displaySellerProducts();

            System.out.print("\nEnter the number of the product you want to delete (or 0 to exit): ");
            String input = scanner.nextLine();

            if (input.equals("0")) {
                System.out.println("Delete product cancelled.");
                break;
            }

            try {
                int productIndex = Integer.parseInt(input);

                if (productIndex < 1 || productIndex > products.size()) {
                    System.out.println("Invalid input!");
                    continue;
                }

                System.out.print("Are you sure you want to delete this product? (Y/N): ");
                String confirm = scanner.nextLine().toUpperCase();

                if (confirm.equals("Y")) {
                    Product deletedProduct = removeProduct(productIndex - 1);
                    stockManager.removeStock(deletedProduct.getName());

                    // Save products to file after deletion
                    FileManager.saveProducts(products);

                    System.out.println("\nProduct \"" + deletedProduct.getName() + "\" was deleted");
                } else {
                    System.out.println("\nDeletion cancelled.");
                }

                System.out.print("\nDo you want to delete another product? (Y/N): ");
                String deleteAnother = scanner.nextLine().toUpperCase();
                if (!deleteAnother.equals("Y")) {
                    deleting = false;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    public void handleProductInformation() {
        boolean viewingInfo = true;

        while (viewingInfo) {
            System.out.println("\n===== PRODUCT INFORMATION =====");
            System.out.println("0. Exit product information");
            System.out.println("1. Display all products");
            System.out.println("2. Search product by name");
            System.out.print("Enter your choice (0-2): ");

            String input = scanner.nextLine();

            if (input.equals("0")) {
                System.out.println("Exiting product information.");
                break;
            }

            if (!input.equals("1") && !input.equals("2")) {
                System.out.println("Please pick a valid option");
                continue;
            }

            if (input.equals("1")) {
                displayAllProductInformation();
            } else {
                System.out.print("Enter product name (or 0 to exit): ");
                String productName = scanner.nextLine();

                if (productName.equals("0")) {
                    System.out.println("Search cancelled.");
                    continue;
                }

                boolean found = false;
                for (Product product : products) {
                    if (product.getName().equalsIgnoreCase(productName)) {
                        displayProductInformation(product);
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    System.out.println("Product does not exist.");
                }
            }

            System.out.print("\nDo you want to view more product information? (Y/N): ");
            String viewMore = scanner.nextLine().toUpperCase();
            if (!viewMore.equals("Y")) {
                viewingInfo = false;
            }
        }
    }

    public void handleSearchStocks(StockManager stockManager) {
        boolean searching = true;

        while (searching) {
            System.out.println("\n===== SEARCH STOCKS =====");
            System.out.println("0. Exit search");

            if (stockManager.getStocks().isEmpty()) {
                System.out.println("No stocks exist. You need to add products first.");
                break;
            }

            System.out.print("Enter product name to search (or 0 to exit): ");
            String productName = scanner.nextLine();

            if (productName.equals("0")) {
                System.out.println("Search cancelled.");
                break;
            }

            stockManager.searchStock(productName);

            System.out.print("\nDo you want to search another product? (Y/N): ");
            String searchAgain = scanner.nextLine().toUpperCase();
            if (!searchAgain.equals("Y")) {
                searching = false;
            }
        }
    }

    public void displaySellerProducts() {
        System.out.println("\nList of all Seller's Products:");
        System.out.println("--------------------------------------");

        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            System.out.println((i + 1) + ". " + product.getName() + " - ₱" + product.getPrice() + " (Qty: " + product.getQuantity() + ")");
        }
    }

    private void displayAllProductInformation() {
        System.out.println("\n===== ALL PRODUCT INFORMATION =====");

        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        for (Product product : products) {
            displayProductInformation(product);
            System.out.println();
        }
    }

    private void displayProductInformation(Product product) {
        System.out.println("\nProduct: " + product.getName());
        System.out.println("Price: ₱" + product.getPrice());
        System.out.println("Quantity: " + product.getQuantity());
        System.out.println("Arrival Date: " + product.getArrivalDate());
        System.out.println("Seller: " + product.getSeller());
        System.out.println("Location: " + product.getLocation());
        System.out.println("Description: " + product.getDescription());
        System.out.println("Category: " + product.getCategory());
        System.out.println("Perishable: " + (product.isPerishable() ? "Yes" : "No"));
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public Product removeProduct(int index) {
        return products.remove(index);
    }

    public List<Product> getProducts() {
        return products;
    }
}