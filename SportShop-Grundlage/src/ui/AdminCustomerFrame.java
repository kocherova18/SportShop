package ui;

import data.DataManager;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminCustomerFrame extends JFrame {

    private DataManager dataManager;
    private List<User> users;

    private DefaultListModel<User> listModel;
    private JList<User> customerList;

    public AdminCustomerFrame() {

        dataManager = new DataManager();

        setTitle("Kunden verwalten");
        setSize(550, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel titleLabel =
                new JLabel(
                        "Kundenliste",
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        20
                )
        );

        add(titleLabel, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        customerList = new JList<>(listModel);

        add(
                new JScrollPane(customerList),
                BorderLayout.CENTER
        );

        JPanel buttonPanel = new JPanel();

        JButton detailsButton =
                new JButton("Details anzeigen");

        JButton deleteButton =
                new JButton("Kunde löschen");

        buttonPanel.add(detailsButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);

        detailsButton.addActionListener(
                event -> showDetails()
        );

        deleteButton.addActionListener(
                event -> deleteCustomer()
        );

        loadCustomers();
    }

    private void loadCustomers() {

        users = dataManager.loadUsers();

        listModel.clear();

        for (User user : users) {

            if (!user.isAdmin()) {
                listModel.addElement(user);
            }
        }
    }

    private void showDetails() {

        User selectedUser =
                customerList.getSelectedValue();

        if (selectedUser == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Bitte einen Kunden auswählen."
            );

            return;
        }

        String addressText =
                "Keine Adresse gespeichert";

        if (selectedUser.getAddress() != null) {

            addressText =
                    selectedUser.getAddress().toString();
        }

        JOptionPane.showMessageDialog(
                this,
                "Name: " + selectedUser.getName()
                        + "\nE-Mail: " + selectedUser.getEmail()
                        + "\nAdresse: " + addressText,
                "Kundendetails",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void deleteCustomer() {

        User selectedUser =
                customerList.getSelectedValue();

        if (selectedUser == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Bitte einen Kunden auswählen."
            );

            return;
        }

        int answer =
                JOptionPane.showConfirmDialog(
                        this,
                        "Kunde wirklich löschen?",
                        "Kunde löschen",
                        JOptionPane.YES_NO_OPTION
                );

        if (answer != JOptionPane.YES_OPTION) {
            return;
        }

        users.removeIf(
                user -> user.getId()
                        == selectedUser.getId()
        );

        dataManager.saveUsers(users);

        loadCustomers();

        JOptionPane.showMessageDialog(
                this,
                "Kunde wurde gelöscht."
        );
    }
}