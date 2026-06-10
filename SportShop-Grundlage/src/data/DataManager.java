package data;

import model.Order;
import model.Product;
import model.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static final String DATA_FOLDER = "data";
    private static final String USERS_FILE = DATA_FOLDER + "/users.dat";
    private static final String PRODUCTS_FILE = DATA_FOLDER + "/products.dat";
    private static final String ORDERS_FILE = DATA_FOLDER + "/orders.dat";

    public DataManager() {
        File folder = new File(DATA_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public void saveUsers(List<User> users) {
        saveObject(users, USERS_FILE);
    }

    public List<User> loadUsers() {
        return loadList(USERS_FILE);
    }

    public void saveProducts(List<Product> products) {
        saveObject(products, PRODUCTS_FILE);
    }

    public List<Product> loadProducts() {
        return loadList(PRODUCTS_FILE);
    }

    public void saveOrders(List<Order> orders) {
        saveObject(orders, ORDERS_FILE);
    }

    public List<Order> loadOrders() {
        return loadList(ORDERS_FILE);
    }

    private void saveObject(Object object, String filePath) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            outputStream.writeObject(object);
        } catch (Exception exception) {
            System.out.println("Fehler beim Speichern: " + exception.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> loadList(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            return (List<T>) inputStream.readObject();
        } catch (Exception exception) {
            System.out.println("Fehler beim Laden: " + exception.getMessage());
            return new ArrayList<>();
        }
    }
}
