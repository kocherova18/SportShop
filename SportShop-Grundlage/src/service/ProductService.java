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
    	List<Product> result = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().toLowerCase()
                    .contains(searchText.toLowerCase())) {
                result.add(product);
            }
        }
        return result;
    }

    public List<Product> filterProducts(String category, double maxPrice) {
            List<Product> result = new ArrayList<>();
            for (Product product : products) {
                boolean categoryMatch = category == null 
                        || category.isEmpty() 
                        || product.getCategory().equalsIgnoreCase(category);
                boolean priceMatch = product.getPrice() <= maxPrice;
                if (categoryMatch && priceMatch) {
                    result.add(product);
                }
            }
            return result;
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
