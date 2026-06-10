import data.DataManager;
import model.Product;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        DataManager dataManager = new DataManager();

        List<Product> products = dataManager.loadProducts();
        System.out.println("SportShop gestartet.");
        System.out.println("Geladene Produkte: " + products.size());
    }
}
