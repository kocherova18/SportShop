package service;

import model.CartItem;
import model.Product;

import java.util.ArrayList;
import java.util.List;

public class CartService {
    private List<CartItem> cartItems = new ArrayList<>();

    public void addToCart(Product product, int quantity) {
        // TODO Person 3: Produkt hinzufuegen oder Menge erhoehen
    }

    public boolean removeFromCart(int productId) {
        // TODO Person 3: Produkt aus Warenkorb entfernen
        return false;
    }

    public double calculateTotalPrice() {
        double sum = 0.0;
        for (CartItem item : cartItems) {
            sum += item.getSubtotal();
        }
        return sum;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void clearCart() {
        cartItems.clear();
    }
}
