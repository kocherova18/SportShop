package ui;

import model.User;
import service.OrderService;
import service.UserService;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;

public class AdminFrame extends JFrame {

    private final UserService userService;
    private final OrderService orderService;
    private final User currentUser;

    public AdminFrame(
            UserService userService,
            OrderService orderService,
            User currentUser) {

        this.userService = userService;
        this.orderService = orderService;
        this.currentUser = currentUser;

        setTitle("Adminbereich");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(
                new EmptyBorder(10, 10, 10, 10)
        );
        contentPane.setLayout(
                new GridLayout(4, 1, 10, 10)
        );

        setContentPane(contentPane);

        JButton productButton =
                new JButton("Produkte verwalten");

        productButton.addActionListener(event -> {
            AdminProductFrame productFrame =
                    new AdminProductFrame();

            productFrame.setLocationRelativeTo(this);
            productFrame.setVisible(true);
        });

        JButton orderButton =
                new JButton("Bestellungen verwalten");

        orderButton.addActionListener(event -> {
            AdminOrderFrame orderFrame =
                    new AdminOrderFrame(
                            orderService,
                            currentUser
                    );
            orderFrame.setLocationRelativeTo(this);
            orderFrame.setVisible(true);
        });

        JButton customerButton =
                new JButton("Kunden verwalten");

        customerButton.addActionListener(event -> {
            AdminCustomerFrame customerFrame =
                    new AdminCustomerFrame();

            customerFrame.setLocationRelativeTo(this);
            customerFrame.setVisible(true);
        });

        JButton logoutButton =
                new JButton("Ausloggen");

        logoutButton.addActionListener(event -> {
            LoginFrame loginFrame =
                    new LoginFrame(userService);

            loginFrame.setLocationRelativeTo(this);
            loginFrame.setVisible(true);

            dispose();
        });

        contentPane.add(productButton);
        contentPane.add(orderButton);
        contentPane.add(customerButton);
        contentPane.add(logoutButton);
    }
}