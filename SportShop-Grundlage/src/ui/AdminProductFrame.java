package ui;

import data.DataManager;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.Product;
import service.ProductService;

public class AdminProductFrame extends JFrame {

    /*
     * In diesem Ordner werden die ausgewählten
     * Produktbilder gespeichert.
     */
    private static final String IMAGE_FOLDER =
            "data/product-images";

    private ProductService productService;
    private DataManager dataManager;

    private DefaultListModel<Product> listModel;
    private JList<Product> productList;

    private JTextField nameField;
    private JTextField priceField;
    private JTextField categoryField;
    private JTextField descriptionField;

    /*
     * Hier merken wir uns das Bild,
     * das der Admin ausgewählt hat.
     */
    private File selectedImageFile;

    /*
     * Zeigt den Namen des ausgewählten Bildes an.
     */
    private JLabel imageNameLabel;

    public AdminProductFrame() {

        dataManager = new DataManager();

        productService = new ProductService(
                dataManager.loadProducts()
        );

        setTitle("Produkte verwalten");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout(10, 10));

        listModel = new DefaultListModel<>();
        productList = new JList<>(listModel);

        add(
                new JScrollPane(productList),
                BorderLayout.CENTER
        );

        /*
         * Jetzt gibt es fünf Eingabezeilen:
         * Name, Preis, Kategorie,
         * Beschreibung und Produktbild.
         */
        JPanel inputPanel =
                new JPanel(new GridLayout(5, 2, 5, 5));

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

        /*
         * Der Admin kann hier ein Bild auswählen.
         */
        JButton selectImageButton =
                new JButton("Bild auswählen");

        imageNameLabel =
                new JLabel("Kein Bild ausgewählt");

        JPanel imagePanel =
                new JPanel(new FlowLayout(
                        FlowLayout.LEFT
                ));

        imagePanel.add(selectImageButton);
        imagePanel.add(imageNameLabel);

        inputPanel.add(new JLabel("Produktbild:"));
        inputPanel.add(imagePanel);

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

        /*
         * Beim Anklicken eines Produkts werden
         * seine Daten in die Felder geschrieben.
         */
        productList.addListSelectionListener(e -> {

            Product product =
                    productList.getSelectedValue();

            if (product != null) {

                nameField.setText(
                        product.getName()
                );

                priceField.setText(
                        String.valueOf(
                                product.getPrice()
                        )
                );

                categoryField.setText(
                        product.getCategory()
                );

                descriptionField.setText(
                        product.getDescription()
                );

                /*
                 * Beim Auswählen eines Produkts
                 * wurde noch kein neues Bild gewählt.
                 */
                selectedImageFile = null;

                String imagePath =
                        product.getImagePath();

                if (imagePath == null
                        || imagePath.trim().isEmpty()) {

                    imageNameLabel.setText(
                            "Kein Bild gespeichert"
                    );

                } else {

                    File imageFile =
                            new File(imagePath);

                    imageNameLabel.setText(
                            "Aktuell: "
                                    + imageFile.getName()
                    );
                }
            }
        });

        selectImageButton.addActionListener(
                e -> selectImage()
        );

        addButton.addActionListener(
                e -> addProduct()
        );

        editButton.addActionListener(
                e -> editProduct()
        );

        deleteButton.addActionListener(
                e -> deleteProduct()
        );

        refreshList();
    }

    /*
     * Öffnet ein Fenster zur Bildauswahl.
     */
    private void selectImage() {

        JFileChooser fileChooser =
                new JFileChooser();

        FileNameExtensionFilter filter =
                new FileNameExtensionFilter(
                        "Bilddateien (*.png, *.jpg, *.jpeg)",
                        "png",
                        "jpg",
                        "jpeg"
                );

        fileChooser.setFileFilter(filter);

        /*
         * Andere Dateitypen können nicht
         * ausgewählt werden.
         */
        fileChooser.setAcceptAllFileFilterUsed(
                false
        );

        int result =
                fileChooser.showOpenDialog(this);

        if (result
                == JFileChooser.APPROVE_OPTION) {

            selectedImageFile =
                    fileChooser.getSelectedFile();

            imageNameLabel.setText(
                    selectedImageFile.getName()
            );
        }
    }

    private void refreshList() {

        listModel.clear();

        for (Product product
                : productService.getAllProducts()) {

            listModel.addElement(product);
        }
    }

    /*
     * Erstellt ein neues Produkt.
     */
    private void addProduct() {

        try {

            int productId = getNextId();

            double price =
                    Double.parseDouble(
                            priceField.getText()
                    );

            /*
             * Ohne ausgewähltes Bild bleibt
             * der Bildpfad leer.
             *
             * Das Detailfenster zeigt dann
             * automatisch die weiße Standardfläche.
             */
            String imagePath = "";

            if (selectedImageFile != null) {

                imagePath = copyImage(
                        selectedImageFile,
                        productId
                );
            }

            Product product = new Product(
                    productId,
                    nameField.getText(),
                    descriptionField.getText(),
                    price,
                    categoryField.getText(),
                    imagePath
            );

            productService.addProduct(product);

            saveProducts();
            refreshList();
            clearFields();

            JOptionPane.showMessageDialog(
                    this,
                    "Produkt wurde hinzugefügt."
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Preis ist ungültig."
            );

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Das Bild konnte nicht gespeichert werden.",
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /*
     * Bearbeitet ein vorhandenes Produkt.
     */
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

            /*
             * Nur wenn ein neues Bild ausgewählt
             * wurde, wird das alte Bild ersetzt.
             *
             * Ohne neue Auswahl bleibt das
             * bisherige Bild erhalten.
             */
            if (selectedImageFile != null) {

                String newImagePath =
                        copyImage(
                                selectedImageFile,
                                selected.getId()
                        );

                selected.setImagePath(
                        newImagePath
                );
            }

            saveProducts();
            refreshList();
            clearFields();

            JOptionPane.showMessageDialog(
                    this,
                    "Produkt wurde bearbeitet."
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Preis ist ungültig."
            );

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Das Bild konnte nicht gespeichert werden.",
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE
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

    /*
     * Kopiert das ausgewählte Bild in den
     * Ordner data/product-images.
     */
    private String copyImage(
            File sourceFile,
            int productId) throws IOException {

        Path imageFolder =
                Paths.get(IMAGE_FOLDER);

        /*
         * Der Ordner wird automatisch erstellt,
         * wenn er noch nicht existiert.
         */
        Files.createDirectories(imageFolder);

        String extension =
                getFileExtension(
                        sourceFile.getName()
                );

        String newFileName =
                "product_"
                        + productId
                        + "."
                        + extension;

        Path targetPath =
                imageFolder.resolve(
                        newFileName
                );

        Path sourcePath =
                sourceFile.toPath();

        /*
         * Verhindert einen Fehler, falls bereits
         * genau dieselbe Datei ausgewählt wurde.
         */
        if (!sourcePath
                .toAbsolutePath()
                .normalize()
                .equals(
                        targetPath
                                .toAbsolutePath()
                                .normalize()
                )) {

            Files.copy(
                    sourcePath,
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );
        }

        /*
         * Unter Windows werden Backslashes
         * durch normale Schrägstriche ersetzt.
         */
        return targetPath
                .toString()
                .replace("\\", "/");
    }

    /*
     * Liest die Dateiendung aus.
     *
     * Beispiel:
     * schuhbild.png -> png
     */
    private String getFileExtension(
            String fileName) {

        int dotPosition =
                fileName.lastIndexOf('.');

        if (dotPosition == -1) {
            return "png";
        }

        return fileName
                .substring(dotPosition + 1)
                .toLowerCase();
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

        selectedImageFile = null;

        imageNameLabel.setText(
                "Kein Bild ausgewählt"
        );

        productList.clearSelection();
    }
}
