package ui;

import data.DataManager;
import model.Order;
import model.User;
import service.OrderHistoryService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OrderHistoryFrame extends JFrame {

    private DefaultListModel<Order> listModel;
    private JList<Order> orderList;

    public OrderHistoryFrame(User currentUser) {

        setTitle("Meine Bestellungen");
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

        JButton detailsButton =
                new JButton("Details anzeigen");

        add(detailsButton, BorderLayout.SOUTH);

        OrderHistoryService historyService =
                new OrderHistoryService(
                        new DataManager()
                );

        List<Order> orders =
                historyService.getOrdersForUser(
                        currentUser
                );

        for (Order order : orders) {
            listModel.addElement(order);
        }

        detailsButton.addActionListener(e -> {

            Order selectedOrder =
                    orderList.getSelectedValue();

            if (selectedOrder == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Bitte eine Bestellung auswählen."
                );
                return;
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Bestellnummer: "
                            + selectedOrder.getOrderId()
                            + "\nStatus: "
                            + selectedOrder.getStatus()
                            + "\nGesamtpreis: "
                            + selectedOrder.getTotalPrice()
                            + " EUR"
                            + "\nDatum: "
                            + selectedOrder.getOrderDate().format(
                            java.time.format.DateTimeFormatter.ofPattern(
                                    "dd.MM.yyyy HH:mm"
                            )
                    ),
                    "Bestelldetails",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
    }
}