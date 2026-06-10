package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String STATUS_CREATED = "ERSTELLT";
    public static final String STATUS_PAID = "BEZAHLT";
    public static final String STATUS_SHIPPED = "VERSENDET";

    private int orderId;
    private User customer;
    private List<CartItem> items;
    private double totalPrice;
    private String status;
    private LocalDateTime orderDate;

    public Order(int orderId, User customer, List<CartItem> items, String status, LocalDateTime orderDate) {
        this.orderId = orderId;
        this.customer = customer;
        this.items = new ArrayList<>(items);
        this.totalPrice = calculateTotalPrice();
        this.status = status;
        this.orderDate = orderDate;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = new ArrayList<>(items);
        this.totalPrice = calculateTotalPrice();
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public double calculateTotalPrice() {
        double sum = 0.0;
        for (CartItem item : items) {
            sum += item.getSubtotal();
        }
        return sum;
    }

    @Override
    public String toString() {
        return "Bestellung #" + orderId + " von " + customer.getName() + " - " + status;
    }
}
