// ProductCatalog.java
import java.util.List;

public class ProductCatalog {
    public List<Product> getProducts() {
        return FileManager.loadProducts();
    }
}