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
        setSize(430, 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(128, 0, 180), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20))
        );

        JLabel emailLabel = new JLabel("E-mail:");
        emailField = new JTextField();

        JLabel passwordLabel = new JLabel("Passwort:");
        passwordField = new JPasswordField();

        JButton registerButton = new JButton("Registrieren");
        registerButton.addActionListener(e -> openRegisterFrame());

        registerButton.setBackground(new Color(210, 170, 230));

        registerButton.setForeground(Color.BLACK);
        registerButton.setFocusPainted(false);

        JButton loginButton = new JButton("Einloggen");
        loginButton.addActionListener(e -> loginUser());

        loginButton.setBackground(new Color(128, 0, 180));

        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        panel.add(emailLabel);
        panel.add(emailField);

        panel.add(passwordLabel);
        panel.add(passwordField);

        panel.add(registerButton);
        panel.add(loginButton);

        panel.setPreferredSize(new Dimension(330, 170));

        panel.setBackground(Color.WHITE);

        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        backgroundPanel.setBackground(new Color(245, 245, 250));
        backgroundPanel.add(panel);
        setContentPane(backgroundPanel);
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
