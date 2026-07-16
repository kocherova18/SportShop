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
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel emailLabel = new JLabel("E-Mail:");
        emailField = new JTextField();

        JLabel passwordLabel = new JLabel("Passwort:");
        passwordField = new JPasswordField();

        JButton registerButton = new JButton("Registrieren");

        registerButton.addActionListener(e -> registerUser());

        panel.add(emailLabel);
        panel.add(emailField);

        panel.add(passwordLabel);
        panel.add(passwordField);

        panel.add(new JLabel(""));
        panel.add(registerButton);

        add(panel);
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