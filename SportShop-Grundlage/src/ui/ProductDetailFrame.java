package ui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;
import model.Product;
import service.CartService;

public class ProductDetailFrame extends JFrame {

    private static final int IMAGE_SIZE = 250;

    public ProductDetailFrame(
            Product product,
            CartService cartService) {

        setTitle(
                "Produktdetails - "
                        + product.getName()
        );

        setSize(700, 400);
        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLayout(
                new BorderLayout(
                        10,
                        10
                )
        );

        /*
         * Hauptbereich mit Bild und Produktdaten.
         */
        JPanel productPanel =
                new JPanel(
                        new BorderLayout(
                                20,
                                10
                        )
                );

        productPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        10,
                        20
                )
        );

        /*
         * Produktbild auf der linken Seite.
         */
        JLabel imageLabel =
                new JLabel(
                        loadProductImage(
                                product.getImagePath()
                        )
                );

        imageLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        imageLabel.setPreferredSize(
                new Dimension(
                        IMAGE_SIZE,
                        IMAGE_SIZE
                )
        );

        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.WHITE);

        imageLabel.setBorder(
                BorderFactory.createLineBorder(
                        Color.LIGHT_GRAY
                )
        );

        productPanel.add(
                imageLabel,
                BorderLayout.WEST
        );

        /*
         * Produktinformationen auf der rechten Seite.
         */
        JPanel detailPanel =
                new JPanel(
                        new GridLayout(
                                5,
                                2,
                                10,
                                10
                        )
                );

        detailPanel.add(new JLabel("Name:"));
        detailPanel.add(
                new JLabel(product.getName())
        );

        detailPanel.add(
                new JLabel("Beschreibung:")
        );

        detailPanel.add(
                new JLabel(
                        product.getDescription()
                )
        );

        detailPanel.add(new JLabel("Preis:"));

        detailPanel.add(
                new JLabel(
                        String.format(
                                "%.2f €",
                                product.getPrice()
                        )
                )
        );

        detailPanel.add(
                new JLabel("Kategorie:")
        );

        detailPanel.add(
                new JLabel(
                        product.getCategory()
                )
        );

        detailPanel.add(new JLabel("ID:"));

        detailPanel.add(
                new JLabel(
                        String.valueOf(
                                product.getId()
                        )
                )
        );

        productPanel.add(
                detailPanel,
                BorderLayout.CENTER
        );

        add(
                productPanel,
                BorderLayout.CENTER
        );

        /*
         * Buttons unten im Fenster.
         */
        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        JButton cartButton =
                new JButton(
                        "In den Warenkorb"
                );

        JButton closeButton =
                new JButton("Schließen");

        buttonPanel.add(cartButton);
        buttonPanel.add(closeButton);

        add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        closeButton.addActionListener(
                e -> dispose()
        );

        cartButton.addActionListener(e -> {

            cartService.addToCart(
                    product,
                    1
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Produkt wurde zum Warenkorb hinzugefügt."
            );
        });

        setLocationRelativeTo(null);
    }

    /*
     * Lädt das Produktbild und passt es an,
     * ohne es zu verzerren.
     */
private ImageIcon loadProductImage(String imagePath) {

    BufferedImage originalImage = null;

    if (imagePath != null
            && !imagePath.trim().isEmpty()) {

        /*
         * Zuerst wird versucht, den gespeicherten
         * Pfad direkt als Datei zu verwenden.
         */
        File directFile =
                new File(imagePath);

        if (directFile.exists()) {

            try {
                originalImage =
                        ImageIO.read(directFile);

            } catch (IOException e) {
                System.out.println(
                        "Bild konnte nicht gelesen werden: "
                                + directFile.getAbsolutePath()
                );
            }
        }

        /*
         * Falls der direkte Pfad nicht funktioniert,
         * wird nur der Dateiname übernommen.
         */
        if (originalImage == null) {

            String fileName =
                    new File(imagePath).getName();

            String[] possiblePaths = {
                    "src/ui/images/" + fileName,
                    "SportShop-Grundlage/src/ui/images/"
                            + fileName
            };

            for (String path : possiblePaths) {

                File imageFile =
                        new File(path);

                if (imageFile.exists()) {

                    try {
                        originalImage =
                                ImageIO.read(imageFile);

                        if (originalImage != null) {
                            break;
                        }

                    } catch (IOException e) {
                        System.out.println(
                                "Bild konnte nicht gelesen werden: "
                                        + imageFile.getAbsolutePath()
                        );
                    }
                }
            }
        }

        /*
         * Zuletzt wird das Bild als Ressource
         * aus ui/images gesucht.
         */
        if (originalImage == null) {

            String fileName =
                    new File(imagePath).getName();

            URL imageUrl =
                    ProductDetailFrame.class.getResource(
                            "/ui/images/" + fileName
                    );

            if (imageUrl != null) {

                try {
                    originalImage =
                            ImageIO.read(imageUrl);

                } catch (IOException e) {
                    System.out.println(
                            "Bildressource konnte nicht gelesen werden: "
                                    + imageUrl
                    );
                }
            }
        }
    }

    /*
     * Kein gültiges Bild gefunden.
     */
    if (originalImage == null) {
        return createWhiteDefaultImage();
    }

    int originalWidth =
            originalImage.getWidth();

    int originalHeight =
            originalImage.getHeight();

    double widthFactor =
            (double) IMAGE_SIZE
                    / originalWidth;

    double heightFactor =
            (double) IMAGE_SIZE
                    / originalHeight;

    /*
     * Das Seitenverhältnis bleibt erhalten.
     */
    double factor =
            Math.min(
                    widthFactor,
                    heightFactor
            );

    int newWidth =
            Math.max(
                    1,
                    (int) (originalWidth * factor)
            );

    int newHeight =
            Math.max(
                    1,
                    (int) (originalHeight * factor)
            );

    BufferedImage imageCanvas =
            new BufferedImage(
                    IMAGE_SIZE,
                    IMAGE_SIZE,
                    BufferedImage.TYPE_INT_RGB
            );

    Graphics2D graphics =
            imageCanvas.createGraphics();

    /*
     * Weißer Hintergrund.
     */
    graphics.setColor(Color.WHITE);

    graphics.fillRect(
            0,
            0,
            IMAGE_SIZE,
            IMAGE_SIZE
    );

    /*
     * Bild mittig platzieren.
     */
    int xPosition =
            (IMAGE_SIZE - newWidth) / 2;

    int yPosition =
            (IMAGE_SIZE - newHeight) / 2;

    graphics.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BICUBIC
    );

    graphics.setRenderingHint(
            RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY
    );

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
}    /*
     * Wird verwendet, wenn kein Bild vorhanden ist.
     */
private ImageIcon createWhiteDefaultImage() {

    BufferedImage defaultImage =
            new BufferedImage(
                    IMAGE_SIZE,
                    IMAGE_SIZE,
                    BufferedImage.TYPE_INT_RGB
            );

    Graphics2D graphics =
            defaultImage.createGraphics();

    graphics.setColor(Color.WHITE);

    graphics.fillRect(
            0,
            0,
            IMAGE_SIZE,
            IMAGE_SIZE
    );

    graphics.setColor(Color.GRAY);

    String text = "Kein Bild verfügbar";

    FontMetrics fontMetrics =
            graphics.getFontMetrics();

    int textX =
            (IMAGE_SIZE
                    - fontMetrics.stringWidth(text))
                    / 2;

    int textY =
            (IMAGE_SIZE
                    - fontMetrics.getHeight())
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
