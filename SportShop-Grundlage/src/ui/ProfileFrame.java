package ui;

import model.User;
import service.UserService;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

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

    public ProfileFrame(UserService userService, User user) {

        this.userService = userService;
        this.user = user;

        setTitle("Mein Konto");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(8, 1, 10, 10));

        JLabel titleLabel = new JLabel("Mein Konto");

        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));

        titleLabel.setForeground(new Color(128, 0, 180));

        nameLabel = new JLabel("Name: " + user.getName());

        emailLabel = new JLabel("E-Mail: " + user.getEmail());

        roleLabel = new JLabel("Rolle: " + user.getRole());

        addressLabel = new JLabel("Adresse: " + getAddressText());

        JButton editProfileButton = new JButton("Profil bearbeiten");

        editProfileButton.addActionListener(e -> openEditProfileFrame());

        editProfileButton.setBackground(new Color(128, 0, 180));

        editProfileButton.setForeground(Color.WHITE);

        editProfileButton.setFocusPainted(false);

        editProfileButton.setFont(new Font("SansSerif", Font.BOLD,14));

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

        panel.setPreferredSize(new Dimension(430, 420));

        panel.setBackground(Color.WHITE);

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(128, 0, 180), 2),
                        BorderFactory.createEmptyBorder(20, 25, 20, 25)
                )
        );

        URL imageUrl = ProfileFrame.class.getResource("/ui/images/sportShop_background2.jpg");

        if (imageUrl != null) {
            ImageIcon backgroundIcon = new ImageIcon(imageUrl);

            JLabel backgroundLabel = new JLabel(backgroundIcon);

            backgroundLabel.setLayout(new GridBagLayout());

            backgroundLabel.add(panel);

            setContentPane(backgroundLabel);

        } else {

            JPanel backgroundPanel = new JPanel(new GridBagLayout());

            backgroundPanel.setPreferredSize(new Dimension(1000, 600));

            backgroundPanel.setBackground(new Color(235, 235, 240));

            backgroundPanel.add(panel);

            setContentPane(backgroundPanel);
        }
        pack();
        setLocationRelativeTo(null);
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
                    "Fehler", JOptionPane.ERROR_MESSAGE);

            return;
        }

        ChangePasswordFrame changePasswordFrame = new ChangePasswordFrame(userService, user);

        changePasswordFrame.setLocationRelativeTo(this);
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

        editProfileFrame.addWindowListener(
                new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosed(
                            java.awt.event.WindowEvent e) {

                        refreshProfileData();
                    }
                });

        editProfileFrame.setLocationRelativeTo(this);
        editProfileFrame.setVisible(true);
    }

    private void refreshProfileData() {
        nameLabel.setText("Name: " + user.getName());

        emailLabel.setText("E-Mail: " + user.getEmail());

        roleLabel.setText("Rolle: " + user.getRole());

        addressLabel.setText("Adresse: " + getAddressText());
    }
}