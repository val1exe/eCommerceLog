public class SellerSession {
    private static User currentUser;

    // Private constructor to prevent instantiation
    private SellerSession() {}

    // Set the current user for the session
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    // Get the current user
    public static User getCurrentUser() {
        return currentUser;
    }

    // Get the seller's name
    public static String getSellerName() {
        return currentUser != null ? currentUser.getFullName() : "Unknown Seller";
    }

    // Get the seller's location/address
    public static String getSellerLocation() {
        return currentUser != null ? currentUser.getAddress() : "Unknown Location";
    }

    // Clear the session when logging out
    public static void clearSession() {
        currentUser = null;
    }
}