import java.util.ArrayList;
import java.util.List;

public class ShippingManager {
    private List<Shipment> shipments = new ArrayList<>();

    public void handleProductShipping(ProductManager productManager) {
        System.out.println("\n===== PRODUCT SHIPPING =====");

        if (shipments.isEmpty()) {
            System.out.println("No products are currently being shipped. You need to add products first.");
            return;
        }

        System.out.println("Products currently being shipped & delivered:");
        System.out.println("Product\tQuantity\tShip Date\tStatus");
        System.out.println("------------------------------------------");

        for (Shipment shipment : shipments) {
            System.out.println(shipment.getProductName() + "\t" + shipment.getQuantity() + "\t\t"
                    + shipment.getShipDate() + "\t" + shipment.getStatus());
        }

        System.out.print("\nGo back? (Y/N): ");
        String choice = System.console().readLine().toUpperCase();

        if (!choice.equals("Y")) {
            System.out.println("\n===== SHIPMENT DETAILS =====");

            if (productManager.getProducts().isEmpty()) {
                System.out.println("No product details available. You need to add products first.");
                return;
            }

            for (Product product : productManager.getProducts()) {
                System.out.println("\nProduct: " + product.getName());
                System.out.println("- Price: $" + product.getPrice());
                System.out.println("- Quantity: " + product.getQuantity());
                System.out.println("- Estimated date of arrival: " + product.getArrivalDate());
                System.out.println("- Name of seller & place: " + product.getSeller() + ", " + product.getLocation());
            }
        }
    }
}