public class Sale {
    private String date;
    private int quantity;

    public Sale(String date, int quantity) {
        this.date = date;
        this.quantity = quantity;
    }

    // Getters
    public String getDate() { return date; }
    public int getQuantity() { return quantity; }
}
