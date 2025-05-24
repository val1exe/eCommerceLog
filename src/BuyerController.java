import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class BuyerController {
    private Scanner scanner;
    private BuyerSession buyerSession;
    private ProductCatalog productCatalog;
    private ShoppingCart cart;

    public BuyerController() {
        this.scanner = new Scanner(System.in);
        this.buyerSession = new BuyerSession();
        this.productCatalog = new ProductCatalog();
        this.cart = new ShoppingCart();
    }

    public void start(User user) {
        System.out.println("\n===== WELCOME TO THE BUYER SYSTEM =====");

        // Set buyer info from logged in user
        buyerSession.setCurrentUser(user); // Add this line
        buyerSession.setBuyerName(user.getFullName());
        buyerSession.setBuyerLocation(user.getAddress());

        System.out.println("Welcome, " + buyerSession.getBuyerName() + "!");

        // Go to the main menu
        mainMenu();
    }

    private void mainMenu() {
        boolean running = true;

        while (running) {
            System.out.println("\n===== MAIN MENU =====");
            System.out.println("1. Browse all products");
            System.out.println("2. Search for products");
            System.out.println("3. View cart (" + cart.getItemCount() + " items)");
            System.out.println("4. View profile");
            System.out.println("5. View order history");
            System.out.println("6. Logout / Exit");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1: browseProducts(); break;
                case 2: searchProducts(); break;
                case 3: viewCart(); break;
                case 4: viewProfile(); break;
                case 5: viewOrderHistory(); break;
                case 6: logout(); running = false; break;
                default: System.out.println("Invalid choice. Please try again."); break;
            }
        }
    }

    private void browseProducts() {
        System.out.println("\n===== BROWSE PRODUCTS =====");
        List<Product> products = FileManager.loadProducts();

        if (products.isEmpty()) {
            System.out.println("No products available at this time.");
            return;
        }

        displayProductsList(products);
        System.out.print("\nEnter product number to view details (0 to return to menu): ");
        int choice = getIntInput();

        if (choice > 0 && choice <= products.size()) {
            viewProductDetails(products.get(choice - 1));
        }
    }

    private void searchProducts() {
        System.out.println("\n===== SEARCH PRODUCTS =====");
        System.out.print("Enter product name or category to search: ");
        String searchTerm = scanner.nextLine();

        List<Product> allProducts = FileManager.loadProducts();
        List<Product> results = new ArrayList<>();

        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    product.getCategory().toLowerCase().contains(searchTerm.toLowerCase())) {
                results.add(product);
            }
        }

        if (results.isEmpty()) {
            System.out.println("No products found matching '" + searchTerm + "'");
            return;
        }

        System.out.println("\nSearch results for '" + searchTerm + "':");
        displayProductsList(results);
        System.out.print("\nEnter product number to view details (0 to return to menu): ");
        int choice = getIntInput();

        if (choice > 0 && choice <= results.size()) {
            viewProductDetails(results.get(choice - 1));
        }
    }

    private void displayProductsList(List<Product> products) {
        System.out.println("\n--------------------------------------------------");
        System.out.printf("%-4s %-20s %-10s %-8s %-12s\n", "No.", "Name", "Price (₱)", "Quantity", "Category");
        System.out.println("--------------------------------------------------");

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            System.out.printf("%-4d %-20s %-10.2f %-8d %-12s\n",
                    (i + 1),
                    truncate(product.getName(), 18),
                    product.getPrice(),
                    product.getQuantity(),
                    truncate(product.getCategory(), 10));
        }
        System.out.println("--------------------------------------------------");
    }

    private void viewProductDetails(Product product) {
        while (true) {
            System.out.println("\n===== PRODUCT DETAILS =====");
            System.out.println("Name: " + product.getName());
            System.out.println("Price: ₱" + product.getPrice());
            System.out.println("Available Quantity: " + product.getQuantity());
            System.out.println("Seller: " + product.getSeller());
            System.out.println("Location: " + product.getLocation());
            System.out.println("Category: " + product.getCategory());
            System.out.println("Description: " + product.getDescription());
            System.out.println("Perishable: " + (product.isPerishable() ? "Yes" : "No"));

            System.out.println("\nOptions:");
            System.out.println("1. Add to cart");
            System.out.println("2. Return to previous menu");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();

            if (choice == 1) {
                addToCart(product);
                break;
            } else if (choice == 2) {
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addToCart(Product product) {
        System.out.print("\nEnter quantity to add to cart (available: " + product.getQuantity() + "): ");
        int quantity = getIntInput();

        if (quantity <= 0) {
            System.out.println("Invalid quantity. Operation cancelled.");
            return;
        }

        if (quantity > product.getQuantity()) {
            System.out.println("Sorry, only " + product.getQuantity() + " available. Operation cancelled.");
            return;
        }

        cart.addItem(product, quantity);
        System.out.println("\nAdded " + quantity + " " + product.getName() + " to your cart!");

        System.out.print("\nWould you like to view your cart? (Y/N): ");
        String viewCart = scanner.nextLine().toUpperCase();

        if (viewCart.equals("Y")) {
            viewCart();
        }
    }

    private void viewCart() {
        System.out.println("\n===== YOUR SHOPPING CART =====");

        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        cart.displayCart();
        System.out.println("\nOptions:");
        System.out.println("1. Proceed to checkout");
        System.out.println("2. Update item quantity");
        System.out.println("3. Remove item from cart");
        System.out.println("4. Return to main menu");
        System.out.print("Enter your choice: ");

        int choice = getIntInput();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1: checkout(); break;
            case 2: updateCartItem(); break;
            case 3: removeCartItem(); break;
            case 4: break;
            default: System.out.println("Invalid choice. Returning to main menu."); break;
        }
    }

    private void updateCartItem() {
        System.out.print("\nEnter the number of the item to update: ");
        int itemIndex = getIntInput();

        if (itemIndex <= 0 || itemIndex > cart.getItems().size()) {
            System.out.println("Invalid item number.");
            return;
        }

        CartItem item = cart.getItems().get(itemIndex - 1);
        System.out.print("Enter new quantity (0 to remove, max " + item.getProduct().getQuantity() + "): ");
        int newQuantity = getIntInput();

        if (newQuantity < 0 || newQuantity > item.getProduct().getQuantity()) {
            System.out.println("Invalid quantity. Operation cancelled.");
            return;
        }

        if (newQuantity == 0) {
            cart.removeItem(itemIndex - 1);
            System.out.println("Item removed from cart.");
        } else {
            cart.updateItemQuantity(itemIndex - 1, newQuantity);
            System.out.println("Quantity updated.");
        }

        viewCart();
    }

    private void removeCartItem() {
        System.out.print("\nEnter the number of the item to remove: ");
        int itemIndex = getIntInput();

        if (itemIndex <= 0 || itemIndex > cart.getItems().size()) {
            System.out.println("Invalid item number.");
            return;
        }

        cart.removeItem(itemIndex - 1);
        System.out.println("Item removed from cart.");
        viewCart();
    }

    private void checkout() {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty. Nothing to checkout.");
            return;
        }

        System.out.println("\n===== CHECKOUT =====");
        cart.displayCart();

        System.out.println("\nDelivery Address:");
        System.out.println(buyerSession.getBuyerName());
        System.out.println(buyerSession.getBuyerLocation());

        System.out.println("\nPayment Method:");
        System.out.println("1. Credit/Debit Card");
        System.out.println("2. Cash on Delivery");
        System.out.println("3. E-wallet");
        System.out.println("4. Cancel checkout");
        System.out.print("Select payment method: ");

        int paymentChoice = getIntInput();
        scanner.nextLine(); // Consume newline

        if (paymentChoice == 4) {
            System.out.println("Checkout cancelled.");
            return;
        }

        if (paymentChoice < 1 || paymentChoice > 3) {
            System.out.println("Invalid payment method. Checkout cancelled.");
            return;
        }

        String paymentMethod = "";
        switch (paymentChoice) {
            case 1:
                paymentMethod = "Credit/Debit Card";
                System.out.print("Enter card number: ");
                String cardNumber = scanner.nextLine();
                System.out.print("Enter expiration date (MM/YY): ");
                String expDate = scanner.nextLine();
                break;
            case 2:
                paymentMethod = "Cash on Delivery";
                break;
            case 3:
                paymentMethod = "E-wallet";
                System.out.print("Enter e-wallet account: ");
                String eWalletAccount = scanner.nextLine();
                break;
        }

        // Create order items with delivery types
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            System.out.println("\nSelect delivery type for " + cartItem.getProduct().getName() + ":");
            System.out.println("1. Express Delivery (+₱100)");
            System.out.println("2. Standard Delivery");
            System.out.println("3. Bulk Delivery (10+ items, 10% discount)");
            System.out.print("Enter choice (1-3): ");

            int deliveryChoice = getIntInput();
            scanner.nextLine(); // Consume newline

            String deliveryType = "Standard Delivery";
            double itemPrice = cartItem.getProduct().getPrice();

            // Apply delivery type modifiers
            switch (deliveryChoice) {
                case 1:
                    deliveryType = "Express Delivery";
                    itemPrice += 100; // Express delivery fee
                    break;
                case 2:
                    deliveryType = "Standard Delivery";
                    break;
                case 3:
                    deliveryType = "Bulk Delivery";
                    if (cartItem.getQuantity() >= 10) {
                        itemPrice *= 0.9; // 10% discount for bulk
                    }
                    break;
                default:
                    System.out.println("Invalid choice, using Standard Delivery");
            }

            orderItems.add(new OrderItem(
                    cartItem.getProduct().getName(),
                    cartItem.getQuantity(),
                    itemPrice,
                    cartItem.getProduct().getSeller(),
                    deliveryType,
                    cartItem.getProduct().isPerishable()
            ));
        }

        // Calculate total amount
        double totalAmount = 0;
        for (OrderItem item : orderItems) {
            totalAmount += item.getUnitPrice() * item.getQuantity();
        }

        // Create order
        Order order = new Order(
                "ORD" + System.currentTimeMillis(),
                java.time.LocalDate.now().toString(),
                "Pending",
                buyerSession.getBuyerName(),
                buyerSession.getBuyerLocation(),
                paymentMethod,
                totalAmount,
                orderItems
        );

        // Update product quantities in the database
        List<Product> allProducts = FileManager.loadProducts();
        for (CartItem item : cart.getItems()) {
            for (Product product : allProducts) {
                if (product.getName().equals(item.getProduct().getName())) {
                    product.setQuantity(product.getQuantity() - item.getQuantity());
                    break;
                }
            }
        }

        // Save updated product quantities
        FileManager.saveProducts(allProducts);

        // Save the order to main orders.txt
        List<Order> existingOrders = OrderFileManager.loadOrders();
        existingOrders.add(order);
        OrderFileManager.saveOrders(existingOrders);

        // Save the order to pending_orders.txt for sellers to see
        PendingOrderFileManager.savePendingOrder(order);

        // Clear the cart
        cart.clear();

        System.out.println("\n===== ORDER CONFIRMATION =====");
        System.out.println("Order #" + order.getOrderId() + " has been placed successfully!");
        System.out.println("Total Amount: ₱" + order.getTotalAmount());
        System.out.println("Payment Method: " + paymentMethod);

        // Display delivery information
        System.out.println("\nDelivery Details:");
        for (OrderItem item : order.getItems()) {
            System.out.println("- " + item.getProductName() + ": " + item.getDeliveryType() +
                    (item.isPerishable() ? " (Perishable)" : ""));
        }

        System.out.println("\nThank you for your purchase!");
    }

    private void viewProfile() {
        System.out.println("\n===== YOUR PROFILE =====");
        System.out.println("Name: " + buyerSession.getBuyerName());
        System.out.println("Location: " + buyerSession.getBuyerLocation());

        System.out.println("\nOptions:");
        System.out.println("1. Edit profile");
        System.out.println("2. Return to main menu");
        System.out.print("Enter your choice: ");

        int choice = getIntInput();
        scanner.nextLine(); // Consume newline

        if (choice == 1) {
            editProfile();
        }
    }

    private void editProfile() {
        System.out.println("\n===== EDIT PROFILE =====");

        System.out.print("Enter new name (leave blank to keep current): ");
        String name = scanner.nextLine();

        if (!name.isEmpty()) {
            buyerSession.setBuyerName(name);
        }

        System.out.print("Enter new location (leave blank to keep current): ");
        String location = scanner.nextLine();

        if (!location.isEmpty()) {
            buyerSession.setBuyerLocation(location);
        }

        System.out.print("Do you want to change password? (Y/N): ");
        String changePassword = scanner.nextLine().toUpperCase();
        if (changePassword.equals("Y")) {
            changePassword();
        }


    }

    private void changePassword() {
        User currentUser = buyerSession.getCurrentUser();
        UserRepository userRepository = new UserRepository();

        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine();

        if (!currentPassword.equals(currentUser.getPassword())) {
            System.out.println("Current password is incorrect!");
            return;
        }

        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();

        System.out.print("Confirm new password: ");
        String confirmPassword = scanner.nextLine();

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Passwords do not match!");
            return;
        }

        currentUser.setPassword(newPassword);
        userRepository.updateUser(currentUser);
        System.out.println("Password changed successfully!");
    }

    private void viewOrderHistory() {
        System.out.println("\n===== ORDER HISTORY =====");

        // Get orders for the current buyer from OrderFileManager
        List<Order> allOrders = OrderFileManager.loadOrders();
        List<Order> buyerOrders = new ArrayList<>();

        // Filter orders for current buyer
        for (Order order : allOrders) {
            if (order.getBuyerName().equals(buyerSession.getBuyerName())) {
                buyerOrders.add(order);
            }
        }

        if (buyerOrders.isEmpty()) {
            System.out.println("You have no previous orders.");
            return;
        }

        for (int i = 0; i < buyerOrders.size(); i++) {
            Order order = buyerOrders.get(i);

            System.out.println("\nOrder #" + order.getOrderId());
            System.out.println("Date: " + order.getOrderDate());
            System.out.println("Status: " + order.getStatus());
            System.out.println("Total: ₱" + order.getTotalAmount());
            System.out.println("Items:");

            for (OrderItem item : order.getItems()) {
                System.out.println("  - " + item.getQuantity() + "x " + item.getProductName() +
                        " (₱" + item.getUnitPrice() + " each) - Seller: " + item.getSeller());
            }
        }
    }

    private void logout() {
        System.out.println("\n===== LOGGING OUT =====");
        System.out.println("Thank you for shopping with us, " + buyerSession.getBuyerName() + "!");
        System.out.println("Have a great day!");
    }

    private int getIntInput() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine(); // Clear the scanner
            return -1; // Return invalid value
        }
    }

    private String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }


}

