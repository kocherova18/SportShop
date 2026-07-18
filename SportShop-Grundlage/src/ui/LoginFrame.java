package ui;

import model.User;
import service.UserService;
import data.DataManager;
import service.CartService;
import service.OrderService;
import service.InvoiceService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private UserService userService;

    private JTextField emailField;
    private JTextField passwordField;

    public LoginFrame(UserService userService){
        this.userService = userService;

        setTitle("Login");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel emailLabel = new JLabel("E-mail:");
        emailField = new JTextField();

        JLabel passwordLabel = new JLabel("Passwort:");
        passwordField = new JPasswordField();

        JButton registerButton = new JButton("Registrieren");
        registerButton.addActionListener(e -> openRegisterFrame());

        JButton loginButton = new JButton("Einloggen");
        loginButton.addActionListener(e -> loginUser());

        panel.add(emailLabel);
        panel.add(emailField);

        panel.add(passwordLabel);
        panel.add(passwordField);

        panel.add(registerButton);
        panel.add(loginButton);

        add(panel);
    }

    private void loginUser(){
        String email = emailField.getText();
        String password = passwordField.getText();

        try{
            User loggedInUser = userService.login(email, password);

            JOptionPane.showMessageDialog(this, "Login erfolgreich. Wilkommen, " + loggedInUser.getEmail());

            emailField.setText("");
            passwordField.setText("");

            DataManager dataManager = new DataManager();

            CartService cartService = new CartService(dataManager, loggedInUser.getId());
            OrderService orderService = new OrderService(dataManager);
            InvoiceService invoiceService = new InvoiceService();

            if (loggedInUser.isAdmin()) {
                AdminFrame adminFrame = new AdminFrame(
                        userService,
                        orderService,
                        loggedInUser
                );

                adminFrame.setLocationRelativeTo(this);
                adminFrame.setVisible(true);
            } else {
                StartFrame startFrame = new StartFrame(
                        userService,
                        cartService,
                        orderService,
                        invoiceService,
                        loggedInUser
                );

                startFrame.setVisible(true);
            }

            dispose();

        }catch (IllegalArgumentException e){
            JOptionPane.showMessageDialog(this, e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openRegisterFrame() {
        RegisterFrame registerFrame = new RegisterFrame(userService);
        registerFrame.setVisible(true);
    }
}
