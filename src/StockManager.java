import java.util.ArrayList;
import java.util.List;

public class StockManager {
    private List<Stock> stocks = new ArrayList<>();

    public StockManager() {
        // Initialize stocks from products file
        List<Product> products = FileManager.loadProducts();
        for (Product product : products) {
            String symbol = product.getName().substring(0, Math.min(4, product.getName().length())).toUpperCase();
            stocks.add(new Stock(symbol, product.getName(), product.getPrice(), product.getQuantity()));
        }
    }

    public void handleDisplayAllStocks() {
        System.out.println("\n===== ALL PRODUCTS =====");
        if (stocks.isEmpty()) {
            System.out.println("No products available. You need to add products first.");
        } else {
            System.out.println("Symbol\tName\tPrice\tQuantity");
            System.out.println("----------------------------------------------");
            for (Stock stock : stocks) {
                System.out.println(stock.getSymbol() + "\t" + stock.getName() + "\t₱" +
                        stock.getPrice() + "\t" + stock.getQuantity());
            }
        }
    }

    public void searchStock(String productName) {
        boolean stockExists = false;
        for (Stock stock : stocks) {
            if (stock.getName().toLowerCase().contains(productName.toLowerCase())) {
                System.out.println("\nProduct found:");
                System.out.println("Symbol: " + stock.getSymbol());
                System.out.println("Name: " + stock.getName());
                System.out.println("Price: ₱" + stock.getPrice());
                System.out.println("Quantity: " + stock.getQuantity());
                stockExists = true;
            }
        }

        if (!stockExists) {
            System.out.println("\nNo product exists with name: " + productName);
        }
    }

    public void addStock(Stock stock) {
        stocks.add(stock);
    }

    public void removeStock(String productName) {
        for (int i = 0; i < stocks.size(); i++) {
            if (stocks.get(i).getName().equals(productName)) {
                stocks.remove(i);
                break;
            }
        }
    }

    public void updateStockName(String oldName, String newName) {
        for (Stock stock : stocks) {
            if (stock.getName().equals(oldName)) {
                stock.setName(newName);
                break;
            }
        }
    }

    public void updateStockPrice(String productName, double newPrice) {
        for (Stock stock : stocks) {
            if (stock.getName().equals(productName)) {
                stock.setPrice(newPrice);
                break;
            }
        }
    }

    public void updateStockQuantity(String productName, int newQuantity) {
        for (Stock stock : stocks) {
            if (stock.getName().equals(productName)) {
                stock.setQuantity(newQuantity);
                break;
            }
        }
    }

    public List<Stock> getStocks() {
        return stocks;
    }
}