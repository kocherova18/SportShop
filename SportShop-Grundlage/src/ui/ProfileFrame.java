package ui;

import model.User;
import javax.swing.*;
import java.awt.*;
import service.UserService;
import ui.EditProfileFrame;

public class ProfileFrame extends JFrame {
    private User user;
    private UserService userService;

    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel roleLabel;
    private JLabel addressLabel;

    public ProfileFrame(User user) {
        this(null, user);
    }

    public ProfileFrame(UserService userService, User user){
        this.userService = userService;
        this.user = user;

        setTitle("Mein Konto");
        setSize(650, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Mein Konto");
        nameLabel = new JLabel("Name: " + user.getName());
        emailLabel = new JLabel("E-Mail: " + user.getEmail());
        roleLabel = new JLabel("Rolle: " + user.getRole());
        addressLabel = new JLabel("Adresse: " + getAddressText());

        JButton editProfileButton = new JButton("Profil bearbeiten");
        editProfileButton.addActionListener(e -> openEditProfileFrame());

        JButton changePasswordButton = new JButton("Passwort ändern");
        changePasswordButton.addActionListener(e -> openChangePasswordFrame());

        JButton closeButton = new JButton("Schließen");
        closeButton.addActionListener(e -> dispose());

        panel.add(titleLabel);
        panel.add(nameLabel);
        panel.add(emailLabel);
        panel.add(roleLabel);
        panel.add(addressLabel);
        panel.add(editProfileButton);
        panel.add(changePasswordButton);
        panel.add(closeButton);

        add(panel);
    }
    private String getAddressText() {
        if (user.getAddress() == null) {
            return "Keine Adresse gespeichert";
        }

        return user.getAddress().toString();
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

    private void openEditProfileFrame() {
        if (userService == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Profilbearbeitung ist hier nicht verfügbar.",
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        EditProfileFrame editProfileFrame = new EditProfileFrame(userService, user);

        editProfileFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                refreshProfileData();
            }
        });

        editProfileFrame.setVisible(true);
    }

    private void refreshProfileData() {
        nameLabel.setText("Name: " + user.getName());
        emailLabel.setText("E-Mail: " + user.getEmail());
        roleLabel.setText("Rolle: " + user.getRole());
        addressLabel.setText("Adresse: " + getAddressText());
    }
}