package service;

import java.util.ArrayList;
import java.util.List;
import model.CartItem;
import model.Product;
import data.DataManager;
import model.User;

public class CartService {

    // Hier werden alle Produkte aus dem Warenkorb gespeichert.
    private List<CartItem> cartItems;
    private DataManager dataManager;
    private int userId;

    public CartService(DataManager dataManager, int userId) {
        this.dataManager = dataManager;
        this.userId = userId;

        cartItems = dataManager.loadCart(userId);
    }

    public boolean addToCart(Product product, int quantity) {
        // Ein leeres Produkt oder eine falsche Menge soll nicht hinzugefügt werden.
        if (product == null || quantity <= 0) {
            return false;
        }

        CartItem existingItem = findCartItemByProductId(product.getId());

        if (existingItem != null) {
            // Das Produkt ist schon vorhanden, deshalb wird nur die Menge erhöht.
            int newQuantity = existingItem.getQuantity() + quantity;
            existingItem.setQuantity(newQuantity);
        } else {
            // Das Produkt ist noch nicht vorhanden und bekommt eine neue Position.
            CartItem newItem = new CartItem(product, quantity);
            cartItems.add(newItem);
        }

        saveCart();

        return true;
    }

    public boolean removeFromCart(int productId) {
        CartItem item = findCartItemByProductId(productId);

        if (item == null) {
            return false;
        }

        cartItems.remove(item);

        saveCart();

        return true;
    }

    public boolean updateQuantity(int productId, int newQuantity) {
        // Eine Produktmenge muss mindestens 1 sein.
        if (newQuantity <= 0) {
            return false;
        }

        CartItem item = findCartItemByProductId(productId);

        if (item == null) {
            return false;
        }

        item.setQuantity(newQuantity);

        saveCart();

        return true;
    }

    public double calculateTotalPrice() {
        double totalPrice = 0.0;

        for (CartItem item : cartItems) {
            totalPrice += item.getSubtotal();
        }

        return totalPrice;
    }

    public List<CartItem> getCartItems() {
        // Es wird eine Kopie der Liste zurückgegeben.
        return new ArrayList<>(cartItems);
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }

    public void clearCart() {
        cartItems.clear();

        saveCart();
    }

    private void saveCart() {
        dataManager.saveCart(userId, cartItems);
    }

    private CartItem findCartItemByProductId(int productId) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == productId) {
                return item;
            }
        }

        return null;
    }
}
