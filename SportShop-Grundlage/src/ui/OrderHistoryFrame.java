package ui;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import model.Order;
import model.User;
import service.OrderService;

public class OrderHistoryFrame extends JFrame {

    private DefaultListModel<Order> listModel;
    private JList<Order> orderList;

    public OrderHistoryFrame(
            OrderService orderService,
            User currentUser) {

        setTitle("Meine Bestellungen");
        setSize(550, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        listModel = new DefaultListModel<>();
        orderList = new JList<>(listModel);

        add(new JScrollPane(orderList), BorderLayout.CENTER);

        JButton detailsButton =
                new JButton("Details anzeigen");

        add(detailsButton, BorderLayout.SOUTH);

        List<Order> orders =
                orderService.getOrdersForUser(currentUser);

        for (Order order : orders) {
            listModel.addElement(order);
        }

        detailsButton.addActionListener(e -> showDetails());
    }

    private void showDetails() {
        Order selectedOrder =
                orderList.getSelectedValue();

        if (selectedOrder == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Bitte eine Bestellung auswählen."
            );
            return;
        }

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd.MM.yyyy HH:mm"
                );

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
                        + selectedOrder.getOrderDate()
                                .format(formatter),
                "Bestelldetails",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
