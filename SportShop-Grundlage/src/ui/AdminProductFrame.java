package ui;

import data.DataManager;
import model.Product;
import service.ProductService;

import javax.swing.*;
import java.awt.*;

public class AdminProductFrame extends JFrame {

    private ProductService productService;
    private DataManager dataManager;

    private DefaultListModel<Product> listModel;
    private JList<Product> productList;

    private JTextField nameField;
    private JTextField priceField;
    private JTextField categoryField;
    private JTextField descriptionField;

    public AdminProductFrame() {

        dataManager = new DataManager();

        productService = new ProductService(
                dataManager.loadProducts()
        );

        setTitle("Produkte verwalten");
        setSize(650, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout(10, 10));

        listModel = new DefaultListModel<>();
        productList = new JList<>(listModel);

        add(
                new JScrollPane(productList),
                BorderLayout.CENTER
        );

        JPanel inputPanel =
                new JPanel(new GridLayout(4, 2, 5, 5));

        nameField = new JTextField();
        priceField = new JTextField();
        categoryField = new JTextField();
        descriptionField = new JTextField();

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Preis:"));
        inputPanel.add(priceField);

        inputPanel.add(new JLabel("Kategorie:"));
        inputPanel.add(categoryField);

        inputPanel.add(new JLabel("Beschreibung:"));
        inputPanel.add(descriptionField);

        add(inputPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();

        JButton addButton =
                new JButton("Hinzufügen");

        JButton editButton =
                new JButton("Bearbeiten");

        JButton deleteButton =
                new JButton("Löschen");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);

        productList.addListSelectionListener(e -> {
            Product product =
                    productList.getSelectedValue();

            if (product != null) {
                nameField.setText(product.getName());
                priceField.setText(
                        String.valueOf(product.getPrice())
                );
                categoryField.setText(
                        product.getCategory()
                );
                descriptionField.setText(
                        product.getDescription()
                );
            }
        });

        addButton.addActionListener(e -> addProduct());
        editButton.addActionListener(e -> editProduct());
        deleteButton.addActionListener(e -> deleteProduct());

        refreshList();
    }

    private void refreshList() {

        listModel.clear();

        for (Product product
                : productService.getAllProducts()) {

            listModel.addElement(product);
        }
    }

    private void addProduct() {

        try {
            Product product = new Product(
                    getNextId(),
                    nameField.getText(),
                    descriptionField.getText(),
                    Double.parseDouble(
                            priceField.getText()
                    ),
                    categoryField.getText(),
                    ""
            );

            productService.addProduct(product);

            saveProducts();
            refreshList();
            clearFields();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Preis ist ungültig."
            );
        }
    }

    private void editProduct() {

        Product selected =
                productList.getSelectedValue();

        if (selected == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Bitte ein Produkt auswählen."
            );
            return;
        }

        try {
            selected.setName(
                    nameField.getText()
            );

            selected.setPrice(
                    Double.parseDouble(
                            priceField.getText()
                    )
            );

            selected.setCategory(
                    categoryField.getText()
            );

            selected.setDescription(
                    descriptionField.getText()
            );

            saveProducts();
            refreshList();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Preis ist ungültig."
            );
        }
    }

    private void deleteProduct() {

        Product selected =
                productList.getSelectedValue();

        if (selected == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Bitte ein Produkt auswählen."
            );
            return;
        }

        productService.deleteProduct(
                selected.getId()
        );

        saveProducts();
        refreshList();
        clearFields();
    }

    private void saveProducts() {

        dataManager.saveProducts(
                productService.getAllProducts()
        );
    }

    private int getNextId() {

        int highestId = 0;

        for (Product product
                : productService.getAllProducts()) {

            if (product.getId() > highestId) {
                highestId = product.getId();
            }
        }

        return highestId + 1;
    }

    private void clearFields() {

        nameField.setText("");
        priceField.setText("");
        categoryField.setText("");
        descriptionField.setText("");

        productList.clearSelection();
    }
}