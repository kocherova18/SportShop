package ui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.List;
import javax.swing.*;
import model.Product;
import service.CartService;
import service.ProductService;

public class ProductListFrame extends JFrame {

    private JList<Product> productJList;
    private DefaultListModel<Product> listModel;
    private JTextField searchField;
    private JComboBox<String> categoryBox;


    public ProductListFrame(
            ProductService productService,
            CartService cartService) {
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
        listModel = new DefaultListModel<>();
        productJList = new JList<>(listModel);
        productJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        productJList.setCellRenderer(new ProductListRenderer());

        productJList.setFixedCellHeight(120);



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

        Product selectedProduct =
                productJList.getSelectedValue();

        if (selectedProduct == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Bitte ein Produkt auswählen!"
            );

            return;
        }

        new ProductDetailFrame(
                selectedProduct,
                cartService
        ).setVisible(true);
            });


    cartButton.addActionListener(e -> {

        Product selectedProduct =
                productJList.getSelectedValue();

        if (selectedProduct == null) {
            return;
        }

        cartService.addToCart(
                selectedProduct,
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

    private void showProducts(
            List<Product> products) {

        listModel.clear();

        for (Product product : products) {
            listModel.addElement(product);
        }
    }

    private class ProductListRenderer
        extends JPanel
        implements ListCellRenderer<Product> {

    private static final int IMAGE_SIZE = 90;

    private final JLabel imageLabel;
    private final JLabel nameLabel;
    private final JLabel priceLabel;
    private final JLabel categoryLabel;
    private final JLabel descriptionLabel;

    public ProductListRenderer() {

        setLayout(new BorderLayout(15, 5));

        setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        imageLabel = new JLabel();
        imageLabel.setPreferredSize(
                new Dimension(
                        IMAGE_SIZE,
                        IMAGE_SIZE
                )
        );

        imageLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.WHITE);

        imageLabel.setBorder(
                BorderFactory.createLineBorder(
                        Color.LIGHT_GRAY
                )
        );

        JPanel textPanel = new JPanel();

        textPanel.setLayout(
                new BoxLayout(
                        textPanel,
                        BoxLayout.Y_AXIS
                )
        );

        nameLabel = new JLabel();
        priceLabel = new JLabel();
        categoryLabel = new JLabel();
        descriptionLabel = new JLabel();

        nameLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        16
                )
        );

        priceLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        textPanel.add(nameLabel);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(priceLabel);
        textPanel.add(categoryLabel);
        textPanel.add(descriptionLabel);

        add(imageLabel, BorderLayout.WEST);
        add(textPanel, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends Product> list,
            Product product,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        nameLabel.setText(
                product.getName()
        );

        priceLabel.setText(
                String.format(
                        "%.2f €",
                        product.getPrice()
                )
        );

        categoryLabel.setText(
                "Kategorie: "
                        + product.getCategory()
        );

        descriptionLabel.setText(
                product.getDescription()
        );

        imageLabel.setIcon(
                loadProductImage(
                        product.getImagePath()
                )
        );


        if (isSelected) {

            setBackground(
                    list.getSelectionBackground()
            );

            nameLabel.setForeground(
                    list.getSelectionForeground()
            );

            priceLabel.setForeground(
                    list.getSelectionForeground()
            );

            categoryLabel.setForeground(
                    list.getSelectionForeground()
            );

            descriptionLabel.setForeground(
                    list.getSelectionForeground()
            );

        } else {

            setBackground(
                    list.getBackground()
            );

            nameLabel.setForeground(
                    list.getForeground()
            );

            priceLabel.setForeground(
                    list.getForeground()
            );

            categoryLabel.setForeground(
                    list.getForeground()
            );

            descriptionLabel.setForeground(
                    list.getForeground()
            );
        }

        /*
         * Textpanel übernimmt die Farbe
         */
        Component textPanel =
                getComponent(1);

        textPanel.setBackground(
                getBackground()
        );

        return this;
    }
}

private ImageIcon loadProductImage(


        String imagePath) {

    Image originalImage = null;


if (imagePath != null
        && !imagePath.trim().isEmpty()) {

    /*
     * läd das bild
     */
    String resourcePath = imagePath;

    if (!resourcePath.startsWith("/")) {
        resourcePath = "/" + resourcePath;
    }

    URL imageUrl =
            ProductListFrame.class.getResource(
                    resourcePath
            );

    if (imageUrl != null) {

        originalImage =
                new ImageIcon(
                        imageUrl
                ).getImage();
    }


    if (originalImage == null) {

        String fileName =
                new File(imagePath).getName();

        String[] possiblePaths = {
                imagePath,
                "src/ui/images/" + fileName,
                "SportShop-Grundlage/src/ui/images/"
                        + fileName
        };

        for (String path : possiblePaths) {

            File imageFile =
                    new File(path);


            if (imageFile.exists()) {

                originalImage =
                        new ImageIcon(
                                imageFile.getAbsolutePath()
                        ).getImage();

                break;
            }
        }
    }
}
    if (originalImage == null
            || originalImage.getWidth(null) <= 0
            || originalImage.getHeight(null) <= 0) {

        return createWhiteDefaultImage();
    }

    int originalWidth =
            originalImage.getWidth(null);

    int originalHeight =
            originalImage.getHeight(null);

    /*
     * skaliert das bild
     */
    double widthFactor =
            90.0 / originalWidth;

    double heightFactor =
            90.0 / originalHeight;

    double factor =
            Math.min(
                    widthFactor,
                    heightFactor
            );

    int newWidth =
            (int) (originalWidth * factor);

    int newHeight =
            (int) (originalHeight * factor);




    BufferedImage imageCanvas =
            new BufferedImage(
                    90,
                    90,
                    BufferedImage.TYPE_INT_RGB
            );

    Graphics2D graphics =
            imageCanvas.createGraphics();

    graphics.setColor(Color.WHITE);

    graphics.fillRect(
            0,
            0,
            90,
            90
    );

    /*
     * setzt das bild mittig
     */
    int xPosition =
            (90 - newWidth) / 2;

    int yPosition =
            (90 - newHeight) / 2;



graphics.setRenderingHint(
        RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BICUBIC
);

graphics.setRenderingHint(
        RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY
);



//zeigt das bild an
graphics.drawImage(
        originalImage,
        xPosition,
        yPosition,
        newWidth,
        newHeight,
        null
);

    graphics.dispose();

    return new ImageIcon(imageCanvas);
}

private ImageIcon createWhiteDefaultImage() {

    BufferedImage defaultImage =
            new BufferedImage(
                    90,
                    90,
                    BufferedImage.TYPE_INT_RGB
            );

    Graphics2D graphics =
            defaultImage.createGraphics();

    // Weißer Hintergrund
    graphics.setColor(Color.WHITE);
    graphics.fillRect(
            0,
            0,
            90,
            90
    );

    // Grauer Hinweistext
    graphics.setColor(Color.GRAY);

    String text = "Kein Bild";

    FontMetrics fontMetrics =
            graphics.getFontMetrics();

    int textX =
            (90 - fontMetrics.stringWidth(text))
                    / 2;

    int textY =
            (90 - fontMetrics.getHeight())
                    / 2
                    + fontMetrics.getAscent();

    graphics.drawString(
            text,
            textX,
            textY
    );

    graphics.dispose();

    return new ImageIcon(defaultImage);
}
}

