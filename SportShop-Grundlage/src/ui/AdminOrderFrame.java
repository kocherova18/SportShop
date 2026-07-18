package ui;

import model.Order;
import model.User;
import service.OrderService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminOrderFrame extends JFrame {

    private OrderService orderService;
    private User currentUser;

    private DefaultListModel<Order> listModel;
    private JList<Order> orderList;
    private JComboBox<String> statusBox;

    public AdminOrderFrame(
            OrderService orderService,
            User currentUser) {

        this.orderService = orderService;
        this.currentUser = currentUser;

        setTitle("Bestellungen verwalten");
        setSize(550, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        listModel = new DefaultListModel<>();
        orderList = new JList<>(listModel);

        add(
                new JScrollPane(orderList),
                BorderLayout.CENTER
        );

        JPanel bottomPanel = new JPanel();

        String[] statusValues = {
                Order.STATUS_CREATED,
                Order.STATUS_PAID,
                Order.STATUS_SHIPPED
        };

        statusBox = new JComboBox<>(statusValues);

        JButton changeButton =
                new JButton("Status ändern");

        bottomPanel.add(new JLabel("Neuer Status:"));
        bottomPanel.add(statusBox);
        bottomPanel.add(changeButton);

        add(bottomPanel, BorderLayout.SOUTH);

        orderList.addListSelectionListener(e -> {
            Order order = orderList.getSelectedValue();

            if (order != null) {
                statusBox.setSelectedItem(
                        order.getStatus()
                );
            }
        });

        changeButton.addActionListener(e -> changeStatus());

        refreshList();
    }

    private void refreshList() {

        listModel.clear();

        List<Order> orders =
                orderService.getAllOrders(currentUser);

        for (Order order : orders) {
            listModel.addElement(order);
        }
    }

    private void changeStatus() {

        Order selectedOrder =
                orderList.getSelectedValue();

        if (selectedOrder == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Bitte eine Bestellung auswählen."
            );
            return;
        }

        String newStatus =
                statusBox.getSelectedItem().toString();

        boolean changed =
                orderService.updateOrderStatus(
                        currentUser,
                        selectedOrder.getOrderId(),
                        newStatus
                );

        if (changed) {
            refreshList();

            JOptionPane.showMessageDialog(
                    this,
                    "Status wurde geändert."
            );
        }
    }
}