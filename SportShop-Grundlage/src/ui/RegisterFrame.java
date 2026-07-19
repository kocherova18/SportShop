package ui;

import service.UserService;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private UserService userService;

    private JTextField emailField;
    private JPasswordField passwordField;

    public RegisterFrame(UserService userService) {
        this.userService = userService;

        setTitle("Registrierung");
        setSize(430, 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));
        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(128, 0, 180), 2),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20))
        );

        JLabel emailLabel = new JLabel("E-Mail:");
        emailField = new JTextField();

        JLabel passwordLabel = new JLabel("Passwort:");
        passwordField = new JPasswordField();

        JButton registerButton = new JButton("Registrieren");
        registerButton.addActionListener(e -> registerUser());

        registerButton.setBackground(new Color(128, 0, 180));

        registerButton.setForeground(Color.BLACK);
        registerButton.setFocusPainted(false);


        panel.add(emailLabel);
        panel.add(emailField);

        panel.add(passwordLabel);
        panel.add(passwordField);

        panel.add(new JLabel(""));
        panel.add(registerButton);

        panel.setPreferredSize(new Dimension(330, 170));

        panel.setBackground(Color.WHITE);

        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        backgroundPanel.setBackground(new Color(245, 245, 250));
        backgroundPanel.add(panel);
        setContentPane(backgroundPanel);
    }

    private void registerUser() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        try {
            userService.register(email, password);

            JOptionPane.showMessageDialog(
                    this,
                    "Registrierung erfolgreich. Sie können sich jetzt einloggen."
            );

            emailField.setText("");
            passwordField.setText("");

            dispose();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}