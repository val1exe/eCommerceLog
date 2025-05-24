// ShoppingCart.java
import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private List<CartItem> items = new ArrayList<>();

    public boolean isEmpty() { return items.isEmpty(); }
    public int getItemCount() { return items.size(); }
    public List<CartItem> getItems() { return items; }

    public void addItem(Product product, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().getName().equals(product.getName())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItem(product, quantity));
    }

    public void removeItem(int index) { items.remove(index); }

    public void updateItemQuantity(int index, int newQuantity) {
        items.get(index).setQuantity(newQuantity);
    }

    public void displayCart() {
        System.out.println("\n--------------------------------------------------");
        System.out.printf("%-4s %-20s %-10s %-8s %-10s\n", "No.", "Name", "Price (₱)", "Quantity", "Subtotal");
        System.out.println("--------------------------------------------------");

        double total = 0;
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            double subtotal = item.getProduct().getPrice() * item.getQuantity();
            System.out.printf("%-4d %-20s %-10.2f %-8d %-10.2f\n",
                    (i + 1),
                    item.getProduct().getName(),
                    item.getProduct().getPrice(),
                    item.getQuantity(),
                    subtotal);
            total += subtotal;
        }
        System.out.println("--------------------------------------------------");
        System.out.printf("%-42s ₱%.2f\n", "Total:", total);
    }

    public Order createOrder(String buyerName, String deliveryAddress, String paymentMethod,
                             List<String> deliveryTypes) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            CartItem cartItem = items.get(i);
            orderItems.add(new OrderItem(
                    cartItem.getProduct().getName(),
                    cartItem.getQuantity(),
                    cartItem.getProduct().getPrice(),
                    cartItem.getProduct().getSeller(),
                    deliveryTypes.get(i),
                    cartItem.getProduct().isPerishable()
            ));
        }


        double totalAmount = 0;
        for (CartItem item : items) {
            totalAmount += item.getProduct().getPrice() * item.getQuantity();
        }

        return new Order(
                "ORD" + System.currentTimeMillis(),
                java.time.LocalDate.now().toString(),
                "Pending",
                buyerName,
                deliveryAddress,
                paymentMethod,
                totalAmount,
                orderItems
        );
    }

    public void clear() { items.clear(); }
}