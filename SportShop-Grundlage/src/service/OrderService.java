package service;

import model.CartItem;
import model.Order;
import model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private List<Order> orders;

    public OrderService(List<Order> orders) {
        this.orders = orders != null ? orders : new ArrayList<>();
    }

    public Order createOrder(User customer, List<CartItem> cartItems) {
        // TODO Person 3: Bestellung aus Warenkorb erzeugen und speichern
        int newOrderId = orders.size() + 1;
        Order order = new Order(newOrderId, customer, cartItems, Order.STATUS_CREATED, LocalDateTime.now());
        orders.add(order);
        return order;
    }

    public List<Order> getOrdersByUser(User customer) {
        // TODO Person 3: nur Bestellungen dieses Kunden zurueckgeben
        return new ArrayList<>();
    }

    public List<Order> getAllOrders() {
        return orders;
    }

    public boolean updateOrderStatus(int orderId, String status) {
        // TODO Person 4/Admin: Status ERSTELLT, BEZAHLT oder VERSENDET setzen
        return false;
    }
}
