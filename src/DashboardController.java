import java.util.Scanner;

public class DashboardController {
    private final Scanner scanner = new Scanner(System.in);
    private ProductManager productManager = new ProductManager();
    private StockManager stockManager = new StockManager();
    private SalesManager salesManager = new SalesManager();
    private ShippingManager shippingManager = new ShippingManager();
    private PendingOrderManager pendingOrderManager = new PendingOrderManager(shippingManager);

    public void start() {
        dashboard();
    }

    private void dashboard() {
        System.out.println("\n===== DASHBOARD =====");
        displayOptions();
    }

    private void displayOptions() {
        System.out.println("\n===== DISPLAY OPTIONS =====");
        System.out.println("1. Search products");
        System.out.println("2. Display all products");
        System.out.println("3. Display all sales");
        System.out.println("4. Manage Products");
        System.out.println("5. Product Shipping");
        System.out.println("6. Pending Orders");
        System.out.println("7. View Profile");
        System.out.println("8. Logout / Exit");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                productManager.handleSearchStocks(stockManager);
                returnToHome();
                break;
            case 2:
                stockManager.handleDisplayAllStocks();
                returnToHome();
                break;
            case 3:
                salesManager.handleDisplayAllSales();
                returnToHome();
                break;
            case 4:
                boolean shouldReturn = productManager.handleManageProducts(stockManager, this);
                if (shouldReturn) {
                    dashboard();
                }
                break;
            case 5:
                shippingManager.handleProductShipping();
                returnToHome();
                break;
            case 6:
                pendingOrderManager.handlePendingOrders();
                returnToHome();
                break;
            case 7:
                handleViewProfile();  // Changed from handleEditProfile()
                returnToHome();
                break;
            case 8:
                handleLogout();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                displayOptions();
                break;
        }
    }

    private void returnToHome() {
        System.out.print("\nPress Enter to return to the dashboard...");
        scanner.nextLine();
        dashboard();
    }

    private void handleLogout() {
        System.out.println("\n===== LOGGING OUT =====");
        System.out.println("Logging out seller...");
        System.out.println("Thank you for using the Seller Dashboard System!");

        // Instead of System.exit(0), we'll return control to the ECommerceSystem
        // We'll do this by throwing a special exception that the ECommerceSystem can catch
        throw new LogoutException();
    }
    private void handleViewProfile() {
        System.out.println("\n===== YOUR PROFILE =====");
        User currentUser = SellerSession.getCurrentUser();

        // Display profile information
        System.out.println("Username: " + currentUser.getUsername());
        System.out.println("Email: " + currentUser.getEmail());
        System.out.println("Full Name: " + currentUser.getFullName());
        System.out.println("Address: " + currentUser.getAddress());

        System.out.println("\nOptions:");
        System.out.println("1. Edit profile");
        System.out.println("2. Change password");
        System.out.println("3. Return to dashboard");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                handleEditProfile();
                break;
            case 2:
                changePassword(currentUser, new UserRepository());
                break;
            case 3:
                // Return to dashboard
                break;
            default:
                System.out.println("Invalid choice. Returning to dashboard.");
        }
    }



    private void handleEditProfile() {
        System.out.println("\n===== EDIT PROFILE =====");
        User currentUser = SellerSession.getCurrentUser();
        UserRepository userRepository = new UserRepository();

        System.out.print("Enter new full name (leave blank to keep current): ");
        String newName = scanner.nextLine();
        if (!newName.isEmpty()) {
            currentUser.setFullName(newName);
        }

        System.out.print("Enter new address (leave blank to keep current): ");
        String newAddress = scanner.nextLine();
        if (!newAddress.isEmpty()) {
            currentUser.setAddress(newAddress);
        }

        System.out.print("Do you want to change password? (Y/N): ");
        String changePassword = scanner.nextLine().toUpperCase();
        if (changePassword.equals("Y")) {
            changePassword(currentUser, userRepository);
        }

        userRepository.updateUser(currentUser);
        System.out.println("Profile updated successfully!");
    }

    private void changePassword(User user, UserRepository userRepository) {
        // Get current user from session if not passed
        if (user == null) {
            user = SellerSession.getCurrentUser();
            if (user == null) {
                System.out.println("No user logged in!");
                return;
            }
        }

        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine();

        if (!currentPassword.equals(user.getPassword())) {
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

        user.setPassword(newPassword);
        userRepository.updateUser(user);
        System.out.println("Password changed successfully!");
    }

    public void returnToDashboard() {
        dashboard();
    }
}

class LogoutException extends RuntimeException {
    public LogoutException() {
        super("User logged out");
    }
}