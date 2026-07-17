package ui;

import model.Product;

import javax.swing.*;
import java.awt.*;

public class ProductDetailFrame extends JFrame {

    public ProductDetailFrame(Product product) {
        setTitle("Produktdetails - " + product.getName());
        setSize(400, 300);
        setLayout(new GridLayout(6, 2, 10, 10));

        add(new JLabel("Name:"));
        add(new JLabel(product.getName()));

        add(new JLabel("Beschreibung:"));
        add(new JLabel(product.getDescription()));

        add(new JLabel("Preis:"));
        add(new JLabel(product.getPrice() + " €"));

        add(new JLabel("Kategorie:"));
        add(new JLabel(product.getCategory()));

        add(new JLabel("ID:"));
        add(new JLabel(String.valueOf(product.getId())));

        JButton closeButton = new JButton("Schließen");
        add(new JLabel(""));
        add(closeButton);

        closeButton.addActionListener(e -> dispose());

        setLocationRelativeTo(null);
    }
}
