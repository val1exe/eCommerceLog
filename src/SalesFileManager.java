import java.io.*;
import java.util.*;

public class SalesFileManager {
    private static final String SALES_FILE = "Sales.txt";

    public static void saveSales(Map<String, List<Sale>> salesData) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SALES_FILE))) {
            for (Map.Entry<String, List<Sale>> entry : salesData.entrySet()) {
                StringBuilder salesStr = new StringBuilder();
                salesStr.append(entry.getKey()).append("|");

                for (Sale sale : entry.getValue()) {
                    salesStr.append(sale.getDate()).append(",")
                            .append(sale.getQuantity()).append(";");
                }

                String salesLine = salesStr.toString();
                if (salesLine.endsWith(";")) {
                    salesLine = salesLine.substring(0, salesLine.length() - 1);
                }

                writer.println(salesLine);
            }
        } catch (IOException e) {
            System.out.println("Error saving sales data: " + e.getMessage());
        }
    }

    public static Map<String, List<Sale>> loadSales() {
        Map<String, List<Sale>> salesData = new HashMap<>();
        File file = new File(SALES_FILE);

        if (!file.exists()) {
            return salesData;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(SALES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    String productName = parts[0];
                    List<Sale> sales = new ArrayList<>();

                    String[] salesArray = parts[1].split(";");
                    for (String saleStr : salesArray) {
                        String[] saleParts = saleStr.split(",");
                        if (saleParts.length == 2) {
                            sales.add(new Sale(saleParts[0], Integer.parseInt(saleParts[1])));
                        }
                    }

                    salesData.put(productName, sales);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading sales data: " + e.getMessage());
        }

        return salesData;
    }
}