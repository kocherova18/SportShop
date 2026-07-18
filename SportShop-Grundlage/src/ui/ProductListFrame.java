package ui;

import service.CartService;
import model.Product;
import service.ProductService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProductListFrame extends JFrame {

    private ProductService productService;
    private CartService cartService;
    private JList<String> productJList;
    private DefaultListModel<String> listModel;
    private JTextField searchField;
    private JComboBox<String> categoryBox;
    private List<Product> displayedProducts;

    public ProductListFrame(
            ProductService productService,
            CartService cartService) {
        this.cartService = cartService;
        this.productService = productService;
        setTitle("SportShop - Produkte");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Oben: Suche
        JPanel topPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Suchen");
        topPanel.add(new JLabel("Suche:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);

        // Filter
        String[] categories = {"Alle", "T-Shirt", "Hose", "Jacke", "Schuhe"};
        categoryBox = new JComboBox<>(categories);
        JButton filterButton = new JButton("Filtern");
        topPanel.add(new JLabel("Kategorie:"));
        topPanel.add(categoryBox);
        topPanel.add(filterButton);

        add(topPanel, BorderLayout.NORTH);

        // Mitte: Produktliste
        // Mitte: Produktliste
        listModel = new DefaultListModel<>();
        productJList = new JList<>(listModel);

        add(new JScrollPane(productJList), BorderLayout.CENTER);

// Unten: Buttons
        JButton detailButton = new JButton("Details anzeigen");
        JButton cartButton = new JButton("In den Warenkorb");

        detailButton.setEnabled(false);
        cartButton.setEnabled(false);

        productJList.addListSelectionListener(e -> {
            boolean selected =
                    productJList.getSelectedIndex() != -1;

            detailButton.setEnabled(selected);
            cartButton.setEnabled(selected);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(cartButton);
        buttonPanel.add(detailButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Aktionen
        searchButton.addActionListener(e -> {
            String text = searchField.getText();
            List<Product> results = productService.searchProducts(text);
            showProducts(results);
        });

        filterButton.addActionListener(e -> {
            String category = categoryBox.getSelectedItem().toString();
            if (category.equals("Alle")) category = "";
            List<Product> results = productService.filterProducts(category, Double.MAX_VALUE);
            showProducts(results);
        });

        detailButton.addActionListener(e -> {
            int index = productJList.getSelectedIndex();
            if (index >= 0) {
                if (index < displayedProducts.size()) {
                    new ProductDetailFrame(
                            displayedProducts.get(index),
                            cartService
                    ).setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Bitte ein Produkt auswählen!");
            }
        });

        cartButton.addActionListener(e -> {
            int index = productJList.getSelectedIndex();

            if (index < 0) {
                return;
            }

            cartService.addToCart(
                    displayedProducts.get(index),
                    1
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Produkt wurde zum Warenkorb hinzugefügt."
            );
        });

        // Alle Produkte laden
        showProducts(productService.getAllProducts());
        setVisible(true);
    }

    private void showProducts(List<Product> products) {
        displayedProducts = products;

        listModel.clear();

        for (Product product : displayedProducts) {
            listModel.addElement(
                    product.getName()
                            + " - "
                            + product.getPrice()
                            + "€ - "
                            + product.getCategory()
            );
        }
    }
}

