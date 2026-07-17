package data;

import model.User;
import model.Product;
import model.Order;
import model.CartItem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private static final String USERS_FILE = "data/users.dat";
    private static final String PRODUCTS_FILE = "data/products.dat";
    private static final String ORDERS_FILE = "data/orders.dat";

    private void createDataFolderIfNeeded(String filePath) {
        File file = new File(filePath);
        File folder = file.getParentFile();

        if (folder != null && !folder.exists()) {
            folder.mkdirs();
        }
    }

    public void saveUsers(List<User> users) {
        createDataFolderIfNeeded(USERS_FILE);

        try {
            FileOutputStream fs = new FileOutputStream(USERS_FILE);
            ObjectOutputStream out = new ObjectOutputStream(fs);

            out.writeObject(users);

            out.close();
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    public List<User> loadUsers() {
        File file = new File(USERS_FILE);

        if (!file.exists()) {
            return new ArrayList<User>();
        }

        try {
            FileInputStream fs = new FileInputStream(USERS_FILE);
            ObjectInputStream in = new ObjectInputStream(fs);

            List<User> users = (List<User>) in.readObject();

            in.close();
            return users;
        } catch (IOException e) {
            System.err.println(e.toString());
        } catch (ClassNotFoundException e) {
            System.err.println(e.toString());
        }

        return new ArrayList<User>();
    }

    public void saveProducts(List<Product> products) {
        try {
            FileOutputStream fs = new FileOutputStream(PRODUCTS_FILE);
            ObjectOutputStream out = new ObjectOutputStream(fs);

            out.writeObject(products);

            out.close();
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    public List<Product> loadProducts() {
        try {
            FileInputStream fs = new FileInputStream(PRODUCTS_FILE);
            ObjectInputStream in = new ObjectInputStream(fs);

            List<Product> products = (List<Product>) in.readObject();

            in.close();
            return products;
        } catch (IOException e) {
            System.err.println(e.toString());
        } catch (ClassNotFoundException e) {
            System.err.println(e.toString());
        }

        return new ArrayList<Product>();
    }

    public void saveOrders(List<Order> orders) {
        createDataFolderIfNeeded(ORDERS_FILE);
        try {
            FileOutputStream fs = new FileOutputStream(ORDERS_FILE);
            ObjectOutputStream out = new ObjectOutputStream(fs);

            out.writeObject(orders);

            out.close();
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    public List<Order> loadOrders() {
        File file = new File(ORDERS_FILE);

        if (!file.exists()) {
            return new ArrayList<Order>();
        }

        try {
            FileInputStream fs = new FileInputStream(ORDERS_FILE);
            ObjectInputStream in = new ObjectInputStream(fs);

            List<Order> orders = (List<Order>) in.readObject();

            in.close();
            return orders;
        } catch (IOException e) {
            System.err.println(e.toString());
        } catch (ClassNotFoundException e) {
            System.err.println(e.toString());
        }

        return new ArrayList<Order>();
    }

    public void saveCart(
            int userId,
            List<CartItem> cartItems) {

        String cartFile =
                "data/cart_" + userId + ".dat";

        createDataFolderIfNeeded(cartFile);

        try {
            FileOutputStream fs =
                    new FileOutputStream(cartFile);

            ObjectOutputStream out =
                    new ObjectOutputStream(fs);

            out.writeObject(cartItems);

            out.close();

        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    public List<CartItem> loadCart(int userId) {

        String cartFile =
                "data/cart_" + userId + ".dat";

        File file = new File(cartFile);

        if (!file.exists()) {
            return new ArrayList<CartItem>();
        }

        try {
            FileInputStream fs =
                    new FileInputStream(cartFile);

            ObjectInputStream in =
                    new ObjectInputStream(fs);

            List<CartItem> cartItems =
                    (List<CartItem>) in.readObject();

            in.close();

            return cartItems;

        } catch (IOException e) {
            System.err.println(e.toString());

        } catch (ClassNotFoundException e) {
            System.err.println(e.toString());
        }

        return new ArrayList<CartItem>();
    }
}
