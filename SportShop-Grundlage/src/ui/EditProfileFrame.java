package ui;
import model.Address;
import model.User;
import service.UserService;

import javax.swing.*;
import java.awt.*;

public class EditProfileFrame extends JFrame {
    private UserService userService;
    private User user;

    private JTextField nameField;
    private JTextField streetField;
    private JTextField houseNumberField;
    private JTextField zipCodeField;
    private JTextField cityField;
    private JTextField countryField;

    public EditProfileFrame(UserService userService, User user) {
        this.userService = userService;
        this.user = user;

        setTitle("Profil bearbeiten");
        setSize(500, 390);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 10, 10));
        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(128, 0, 180), 2),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20))
        );
        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();

        JLabel streetLabel = new JLabel("Strasse:");
        streetField = new JTextField();

        JLabel houseNumberLabel = new JLabel("Hausnummer:");
        houseNumberField = new JTextField();

        JLabel zipCodeLabel = new JLabel("PLZ:");
        zipCodeField = new JTextField();

        JLabel cityLabel = new JLabel("Stadt:");
        cityField = new JTextField();

        JLabel countryLabel = new JLabel("Land:");
        countryField = new JTextField();

        JButton saveButton = new JButton("Speichern");
        saveButton.addActionListener(e -> saveProfile());

        saveButton.setBackground(new Color(128, 0, 180));
        saveButton.setForeground(Color.BLACK);
        saveButton.setFocusPainted(false);

        JButton cancelButton = new JButton("Abbrechen");
        cancelButton.addActionListener(e -> dispose());

        cancelButton.setBackground(new Color(210, 170, 230));
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setFocusPainted(false);

        fillFieldsWithUserData();

        panel.add(nameLabel);
        panel.add(nameField);

        panel.add(streetLabel);
        panel.add(streetField);

        panel.add(houseNumberLabel);
        panel.add(houseNumberField);

        panel.add(zipCodeLabel);
        panel.add(zipCodeField);

        panel.add(cityLabel);
        panel.add(cityField);

        panel.add(countryLabel);
        panel.add(countryField);

        panel.add(cancelButton);
        panel.add(saveButton);

        panel.setPreferredSize(new Dimension(400, 290));

        panel.setBackground(Color.WHITE);

        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        backgroundPanel.setBackground(new Color(245, 245, 250));
        backgroundPanel.add(panel);
        setContentPane(backgroundPanel);
    }

    private void fillFieldsWithUserData() {
        nameField.setText(getTextOrEmpty(user.getName()));

        Address address = user.getAddress();

        if (address != null) {
            streetField.setText(getTextOrEmpty(address.getStreet()));
            houseNumberField.setText(getTextOrEmpty(address.getHouseNumber()));
            zipCodeField.setText(getTextOrEmpty(address.getZipCode()));
            cityField.setText(getTextOrEmpty(address.getCity()));
            countryField.setText(getTextOrEmpty(address.getCountry()));
        }
    }

    private void saveProfile() {
        String name = nameField.getText();

        Address address = new Address(
                streetField.getText().trim(),
                houseNumberField.getText().trim(),
                zipCodeField.getText().trim(),
                cityField.getText().trim(),
                countryField.getText().trim()
        );

        try {
            userService.updateProfile(user, name, address);

            JOptionPane.showMessageDialog(
                    this,
                    "Profil wurde erfolgreich gespeichert."
            );

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

    private String getTextOrEmpty(String text) {
        if (text == null) {
            return "";
        }

        return text;
    }
}
