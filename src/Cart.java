import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public void addItem(Product product, int quantity) {
        // Check if the product is already in the cart
        for (CartItem item : items) {
            if (item.getProduct().getName().equals(product.getName())) {
                // Update quantity instead of adding a new item
                item.setQuantity(item.getQuantity() + quantity);
                System.out.println("Updated quantity in cart for " + product.getName());
                return;
            }
        }

        // If not already in cart, add new item
        items.add(new CartItem(product, quantity));
        System.out.println("Added " + product.getName() + " to cart");
    }

    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            CartItem removed = items.remove(index);
            System.out.println("Removed " + removed.getProduct().getName() + " from cart");
        } else {
            System.out.println("Invalid item index");
        }
    }

    public void updateItemQuantity(int index, int newQuantity) {
        if (index >= 0 && index < items.size()) {
            if (newQuantity <= 0) {
                CartItem removed = items.remove(index);
                System.out.println("Removed " + removed.getProduct().getName() + " from cart");
            } else {
                items.get(index).setQuantity(newQuantity);
                System.out.println("Updated quantity for " + items.get(index).getProduct().getName());
            }
        } else {
            System.out.println("Invalid item index");
        }
    }

    public void displayCart() {
        if (items.isEmpty()) {
            System.out.println("Your cart is empty");
            return;
        }

        System.out.println("\n===== YOUR CART =====");
        System.out.printf("%-5s %-20s %-10s %-10s %-15s %-10s\n",
                "Item", "Product", "Price", "Quantity", "Seller", "Total");
        System.out.println("---------------------------------------------------------------------");

        double grandTotal = 0;
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            Product product = item.getProduct();
            double itemTotal = product.getPrice() * item.getQuantity();
            grandTotal += itemTotal;

            System.out.printf("%-5d %-20s ₱%-10.2f %-10d %-15s ₱%-10.2f\n",
                    (i + 1),
                    product.getName(),
                    product.getPrice(),
                    item.getQuantity(),
                    product.getSeller(),
                    itemTotal);
        }

        System.out.println("---------------------------------------------------------------------");
        System.out.printf("GRAND TOTAL: ₱%.2f\n", grandTotal);
    }

    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
        System.out.println("Cart cleared");
    }

    public List<CartItem> getItems() {
        return items;
    }

    public Order createOrder(String buyerName, String deliveryAddress, String paymentMethod) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : items) {
            Product product = cartItem.getProduct();
            OrderItem orderItem = new OrderItem(
                    product.getName(),
                    cartItem.getQuantity(),
                    product.getPrice(),
                    product.getSeller()
            );
            orderItems.add(orderItem);
        }

        return new Order(buyerName, deliveryAddress, paymentMethod, orderItems);
    }
}