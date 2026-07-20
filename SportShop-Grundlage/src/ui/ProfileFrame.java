package ui;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import model.User;
import service.UserService;

public class ProfileFrame extends JFrame {

    private static final String BACKGROUND_IMAGE_NAME =
            "sportShop_background2.jpg";

    private static final String BACKGROUND_IMAGE_PATH =
            "/ui/images/" + BACKGROUND_IMAGE_NAME;

    private User user;
    private UserService userService;

    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel roleLabel;
    private JLabel addressLabel;

    public ProfileFrame(User user) {
        this(null, user);
        pack();
        setLocationRelativeTo(null);
    }

    public ProfileFrame(UserService userService, User user){
        this.userService = userService;
        this.user = user;

        setTitle("Mein Konto");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Mein Konto");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        titleLabel.setFont(
                new Font("SansSerif", Font.BOLD, 26)
        );

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
        editProfileButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        JButton changePasswordButton = new JButton("Passwort ändern");
        changePasswordButton.addActionListener(e -> openChangePasswordFrame());

        changePasswordButton.setBackground(new Color(254, 52, 203));
        changePasswordButton.setForeground(Color.WHITE);
        changePasswordButton.setFocusPainted(false);
        changePasswordButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        JButton closeButton = new JButton("Schließen");
        closeButton.addActionListener(e -> dispose());

        closeButton.setBackground(new Color(255, 111, 111));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setFont(new Font("SansSerif", Font.BOLD, 14));

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

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(128, 0, 180), 2),
                        BorderFactory.createEmptyBorder(20, 25, 20, 25)
                )
        );


        Image backgroundImage = loadBackgroundImage();

        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImage);

        backgroundPanel.setLayout(new GridBagLayout());

        backgroundPanel.add(panel);

        setContentPane(backgroundPanel);

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

    private Image loadBackgroundImage() {
        URL imageUrl =
                ProfileFrame.class.getResource(
                        BACKGROUND_IMAGE_PATH
                );

        if (imageUrl != null) {
            return new ImageIcon(imageUrl).getImage();
        }

        String[] possiblePaths = {
                "src/ui/images/" + BACKGROUND_IMAGE_NAME,
                "SportShop-Grundlage/src/ui/images/"
                        + BACKGROUND_IMAGE_NAME
        };

        for (String path : possiblePaths) {
            java.io.File imageFile =
                    new java.io.File(path);

            if (imageFile.exists()) {
                return new ImageIcon(path).getImage();
            }
        }

        JOptionPane.showMessageDialog(
                this,
                "Das Hintergrundbild wurde nicht gefunden.\n"
                        + "Erwarteter Ort:\n"
                        + "src/ui/images/"
                        + BACKGROUND_IMAGE_NAME,
                "Bild fehlt",
                JOptionPane.ERROR_MESSAGE
        );

        return null;
    }


    private static class BackgroundPanel extends JPanel {

        private final Image backgroundImage;

        public BackgroundPanel(Image backgroundImage) {

            this.backgroundImage = backgroundImage;

            setPreferredSize(new Dimension(1000, 600));
        }

        @Override
        protected void paintComponent(Graphics graphics) {

            super.paintComponent(graphics);

            if (backgroundImage == null) {
                return;
            }

            graphics.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}