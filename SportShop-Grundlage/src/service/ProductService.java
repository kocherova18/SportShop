package service;

import java.util.ArrayList;
import java.util.List;

import model.Product;

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

        if (searchText == null || searchText.isBlank()) {
            result.addAll(products);
            return result;
        }

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
            boolean categoryMatch =
                    category == null
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
        if (product == null || getProductById(product.getId()) != null) {
            return false;
        }

        products.add(product);
        return true;
    }

    public boolean updateProduct(Product updatedProduct) {
        if (updatedProduct == null) {
            return false;
        }

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == updatedProduct.getId()) {
                products.set(i, updatedProduct);
                return true;
            }
        }

        return false;
    }

    public boolean deleteProduct(int productId) {
        return products.removeIf(product -> product.getId() == productId);
    }
}
