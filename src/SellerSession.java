public class SellerSession {
    private static String sellerName;
    private static String sellerLocation;

    // Initialize with default values or from a login
    public static void initializeSession(String name, String location) {
        sellerName = name;
        sellerLocation = location;
    }

    public static String getSellerName() {
        // Return a default if not set
        return (sellerName != null) ? sellerName : "Default Seller";
    }

    public static String getSellerLocation() {
        // Return a default if not set
        return (sellerLocation != null) ? sellerLocation : "Default Location";
    }

    public static void setSellerName(String name) {
        sellerName = name;
    }

    public static void setSellerLocation(String location) {
        sellerLocation = location;
    }
}