package service;

import data.DataManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.CartItem;
import model.Order;
import model.User;

public class OrderService {

    private List<Order> orders;
    private DataManager dataManager;

    public OrderService(DataManager dataManager) {
        if (dataManager == null) {
            throw new IllegalArgumentException(
                    "DataManager darf nicht null sein."
            );
        }

        this.dataManager = dataManager;
        this.orders = dataManager.loadOrders();

        // Falls nichts geladen werden konnte, wird eine leere Liste benutzt.
        if (orders == null) {
            orders = new ArrayList<>();
        }
    }

    public Order createOrder(
            User currentUser,
            List<CartItem> cartItems) {

        // Nur ein angemeldeter Kunde kann eine Bestellung erstellen.
        if (currentUser == null
                || !User.ROLE_CUSTOMER.equals(currentUser.getRole())) {
            return null;
        }

        if (currentUser.getAddress() == null) {
            return null;
        }

        // Ein leerer oder fehlender Warenkorb kann nicht bestellt werden.
        if (cartItems == null || cartItems.isEmpty()) {
            return null;
        }

        if (!areCartItemsValid(cartItems)) {
            return null;
        }

        int orderId = getNextOrderId();
        List<CartItem> orderItems = copyCartItems(cartItems);

        Order newOrder = new Order(
                orderId,
                currentUser,
                orderItems,
                Order.STATUS_CREATED,
                LocalDateTime.now()
        );

        orders.add(newOrder);
        saveOrders();

        return newOrder;
    }

    public List<Order> getOrdersForUser(User currentUser) {
        List<Order> userOrders = new ArrayList<>();

        if (currentUser == null) {
            return userOrders;
        }

        for (Order order : orders) {
            if (isOrderOwnedByUser(order, currentUser)) {
                userOrders.add(order);
            }
        }

        return userOrders;
    }

    public Order getOrderForUser(
            User currentUser,
            int orderId) {

        if (currentUser == null) {
            return null;
        }

        Order order = findOrderById(orderId);

        if (!isOrderOwnedByUser(order, currentUser)) {
            return null;
        }

        return order;
    }

    public List<Order> getAllOrders(User currentUser) {
        // Nur ein Admin darf alle Bestellungen abrufen.
        if (currentUser == null || !currentUser.isAdmin()) {
            return new ArrayList<>();
        }

        return new ArrayList<>(orders);
    }

    public boolean updateOrderStatus(
            User currentUser,
            int orderId,
            String newStatus) {

        // Kunden dürfen den Status nicht selbst ändern.
        if (currentUser == null || !currentUser.isAdmin()) {
            return false;
        }

        if (!isValidStatus(newStatus)) {
            return false;
        }

        Order order = findOrderById(orderId);

        if (order == null) {
            return false;
        }

        order.setStatus(newStatus);
        saveOrders();

        return true;
    }

    private Order findOrderById(int orderId) {
        for (Order order : orders) {
            if (order != null
                    && order.getOrderId() == orderId) {
                return order;
            }
        }

        return null;
    }

    private boolean isOrderOwnedByUser(
            Order order,
            User currentUser) {

        if (order == null
                || currentUser == null
                || order.getCustomer() == null) {
            return false;
        }

        return order.getCustomer().getId()
                == currentUser.getId();
    }

    private int getNextOrderId() {
        int highestOrderId = 0;

        for (Order order : orders) {
            if (order != null
                    && order.getOrderId() > highestOrderId) {
                highestOrderId = order.getOrderId();
            }
        }

        return highestOrderId + 1;
    }

    private boolean areCartItemsValid(
            List<CartItem> cartItems) {

        for (CartItem item : cartItems) {
            if (item == null
                    || item.getProduct() == null
                    || item.getQuantity() <= 0) {
                return false;
            }
        }

        return true;
    }

    private List<CartItem> copyCartItems(
            List<CartItem> cartItems) {

        List<CartItem> orderItems = new ArrayList<>();

        for (CartItem item : cartItems) {
            CartItem copiedItem = new CartItem(
                    item.getProduct(),
                    item.getQuantity()
            );

            orderItems.add(copiedItem);
        }

        return orderItems;
    }

    private boolean isValidStatus(String status) {
        return Order.STATUS_CREATED.equals(status)
                || Order.STATUS_PAID.equals(status)
                || Order.STATUS_SHIPPED.equals(status);
    }

    private void saveOrders() {
        dataManager.saveOrders(orders);
    }
}
