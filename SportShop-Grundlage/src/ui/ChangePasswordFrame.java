package ui;

import service.UserService;
import model.User;
import javax.swing.*;
import java.awt.*;

public class ChangePasswordFrame extends JFrame {
    private UserService userService;
    private User user;

    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;

    public ChangePasswordFrame(UserService userService, User user) {
        this.userService = userService;
        this.user = user;

        setTitle("Passwort ändern");
        setSize(460, 270);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3,2,10,10));
        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(128, 0, 180), 2),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20))
        );

        JLabel oldPasswordLabel = new JLabel("Altes Passwort:");
        oldPasswordField = new JPasswordField();

        JLabel newPasswordLabel = new JLabel("Neues Passwort:");
        newPasswordField = new JPasswordField();

        JButton saveButton = new JButton("Speichern");
        saveButton.addActionListener(e -> changePassword());

        saveButton.setBackground(new Color(128, 0, 180));
        saveButton.setForeground(Color.BLACK);
        saveButton.setFocusPainted(false);

        JButton cancelButton = new JButton("Abbrechen");
        cancelButton.addActionListener(e -> dispose());

        cancelButton.setBackground(new Color(210, 170, 230));
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setFocusPainted(false);

        panel.add(oldPasswordLabel);
        panel.add(oldPasswordField);

        panel.add(newPasswordLabel);
        panel.add(newPasswordField);

        panel.add(cancelButton);
        panel.add(saveButton);

        panel.setPreferredSize(new Dimension(360, 180));

        panel.setBackground(Color.WHITE);

        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        backgroundPanel.setBackground(new Color(245, 245, 250));
        backgroundPanel.add(panel);
        setContentPane(backgroundPanel);
    }

    private void changePassword() {
        String oldPassword = new String(oldPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());

        try {
            userService.changePassword(user, oldPassword, newPassword);

            JOptionPane.showMessageDialog(
                    this,
                    "Passwort wurde erfolgreich geändert."
            );

            oldPasswordField.setText("");
            newPasswordField.setText("");

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
