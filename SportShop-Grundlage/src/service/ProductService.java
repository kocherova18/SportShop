package service;

import model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductService {
    private List<Product> products;

    public ProductService(List<Product> products) {
        this.products = products != null ? products : new ArrayList<>();
    }

    public List<Product> getAllProducts() {
        return products;
    }

    public Product getProductById(int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    public List<Product> searchProducts(String searchText) {
        // TODO Person 2: Suche nach Produktname implementieren
        return new ArrayList<>();
    }

    public List<Product> filterProducts(String category, double maxPrice) {
        // TODO Person 2: Filter nach Kategorie und Preis implementieren
        return new ArrayList<>();
    }

    public boolean addProduct(Product product) {
        // TODO Person 4/Admin: Produkt pruefen und hinzufuegen
        return false;
    }

    public boolean updateProduct(Product product) {
        // TODO Person 4/Admin: vorhandenes Produkt aktualisieren
        return false;
    }

    public boolean deleteProduct(int productId) {
        // TODO Person 4/Admin: Produkt loeschen
        return false;
    }
}
