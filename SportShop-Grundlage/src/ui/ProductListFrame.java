package ui;

import model.Product;
import service.ProductService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProductListFrame extends JFrame {

    private ProductService productService;
    private JList<String> productJList;
    private DefaultListModel<String> listModel;
    private JTextField searchField;
    private JComboBox<String> categoryBox;

    public ProductListFrame(ProductService productService) {
        this.productService = productService;
        setTitle("SportShop - Produkte");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        listModel = new DefaultListModel<>();
        productJList = new JList<>(listModel);
        add(new JScrollPane(productJList), BorderLayout.CENTER);

        // Unten: Button
        JButton detailButton = new JButton("Details anzeigen");
        add(detailButton, BorderLayout.SOUTH);

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
                List<Product> all = productService.getAllProducts();
                if (index < all.size()) {
                    new ProductDetailFrame(all.get(index)).setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Bitte ein Produkt auswählen!");
            }
        });

        // Alle Produkte laden
        showProducts(productService.getAllProducts());
        setVisible(true);
    }

    private void showProducts(List<Product> products) {
        listModel.clear();
        for (Product p : products) {
            listModel.addElement(p.getName() + " - " + p.getPrice() + "€ - " + p.getCategory());
        }
    }
}
