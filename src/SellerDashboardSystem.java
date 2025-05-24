public class SellerDashboardSystem {
    public static void main(String[] args) {
        try {
            DashboardController controller = new DashboardController();
            controller.start();
        } catch (LogoutException e) {
            // Return to ECommerceSystem main menu
            ECommerceSystem.main(new String[]{});
        }
    }
}