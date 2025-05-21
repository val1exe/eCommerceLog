import java.util.Scanner;

public class DashboardController {
    private Scanner scanner = new Scanner(System.in);
    private ProductManager productManager = new ProductManager();
    private StockManager stockManager = new StockManager();
    private SalesManager salesManager = new SalesManager();
    private ShippingManager shippingManager = new ShippingManager();
    private PendingOrderManager pendingOrderManager = new PendingOrderManager();
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
        System.out.println("7. Logout / Exit");
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
                salesManager.handleDisplayAllSales(productManager);
                returnToHome();
                break;
            case 4:
                boolean shouldReturn = productManager.handleManageProducts(stockManager, salesManager, this);
                if (shouldReturn) {
                    dashboard();
                }
                break;
            case 5:
                shippingManager.handleProductShipping(productManager);
                returnToHome();
                break;
            case 6:
                pendingOrderManager.handlePendingOrders(); // Add this case
                returnToHome();
                break;
            case 7: // Change from 6 to 7
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
        System.out.println("END");
        System.exit(0);
    }

    public void returnToDashboard() {
        dashboard();
    }
}