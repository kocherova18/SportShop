package service;

import data.DataManager;
import model.Order;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryService {

    private DataManager dataManager;

    public OrderHistoryService(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public List<Order> getOrdersForUser(User user) {

        List<Order> result = new ArrayList<>();

        if (user == null) {
            return result;
        }

        List<Order> allOrders =
                dataManager.loadOrders();

        for (Order order : allOrders) {
            if (order.getCustomer() != null
                    && order.getCustomer().getId() == user.getId()) {

                result.add(order);
            }
        }

        return result;
    }
}