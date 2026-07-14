package ui;

import model.User;
import javax.swing.*;
import java.awt.*;
import service.UserService;

public class ProfileFrame extends JFrame {
    private User user;
    private UserService userService;

    public ProfileFrame(User user) {
        this(null, user);
    }

    public ProfileFrame(UserService userService, User user){
        this.userService = userService;
        this.user = user;

        setTitle("Mein Konto");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Mein Konto");
        JLabel nameLabel = new JLabel("Name: " + user.getName());
        JLabel emailLabel = new JLabel("E-Mail: " + user.getEmail());
        JLabel roleLabel = new JLabel("Rolle: " + user.getRole());

        JButton changePasswordButton = new JButton("Passwort ändern");
        changePasswordButton.addActionListener(e -> openChangePasswordFrame());

        JButton closeButton = new JButton("Schließen");
        closeButton.addActionListener(e -> dispose());

        panel.add(titleLabel);
        panel.add(nameLabel);
        panel.add(emailLabel);
        panel.add(roleLabel);
        panel.add(changePasswordButton);
        panel.add(closeButton);

        add(panel);
    }
    private void openChangePasswordFrame() {
        if (userService == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Passwortänderung ist hier nicht verfügbar.",
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        ChangePasswordFrame changePasswordFrame = new ChangePasswordFrame(userService, user);
        changePasswordFrame.setVisible(true);
    }

}