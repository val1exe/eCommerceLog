import java.util.ArrayList;
import java.util.List;

public class BuyerSession {
    private static String buyerName;
    private static String buyerLocation;
    private static List<Order> orders = new ArrayList<>();

    // Initialize with default values or from a login
    public static void initializeSession(String name, String location) {
        buyerName = name;
        buyerLocation = location;
    }

    public static String getBuyerName() {
        // Return a default if not set
        return (buyerName != null) ? buyerName : "Default Buyer";
    }

    public static String getBuyerLocation() {
        // Return a default if not set
        return (buyerLocation != null) ? buyerLocation : "Default Location";
    }

    public static void setBuyerName(String name) {
        buyerName = name;
    }

    public static void setBuyerLocation(String location) {
        buyerLocation = location;
    }

    public static void addOrder(Order order) {
        orders.add(order);
    }

    public static List<Order> getOrders() {
        return orders;
    }
}