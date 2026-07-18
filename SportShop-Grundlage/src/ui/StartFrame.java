package ui;


import data.DataManager;
import service.ProductService;
import java.util.ArrayList;
import java.util.List;
import model.Product;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import model.User;
import service.CartService;
import service.InvoiceService;
import service.OrderService;
import service.UserService;


public class StartFrame extends JFrame {

    private static final String BACKGROUND_IMAGE_PATH =
            "/ui/images/nike-wm.png";

    private static final String CART_ICON_PATH =
            "/ui/images/cart-symbol.png";

    private static final int TOP_PANEL_HEIGHT = 70;

    private static final boolean SHOW_CART_BUTTON = true;

    private final UserService userService;
    private final CartService cartService;
    private final OrderService orderService;
    private final InvoiceService invoiceService;
    private final ProductService productService;
    private final User currentUser;

    private CartFrame cartFrame;


    public StartFrame(
            UserService userService,
            CartService cartService,
            OrderService orderService,
            InvoiceService invoiceService,
            User currentUser) {

        if (userService == null
                || cartService == null
                || orderService == null
                || invoiceService == null
                || currentUser == null) {

            throw new IllegalArgumentException(
                    "StartFrame benötigt alle Services "
                            + "und einen Benutzer."
            );
        }

        this.userService = userService;
        this.cartService = cartService;
        this.orderService = orderService;
        this.invoiceService = invoiceService;
        this.productService =
                new ProductService(new DataManager().loadProducts());
        this.currentUser = currentUser;

        initializeUI();
    }


    /*
     * Erstellt das gesamte Fenster.
     */
    private void initializeUI() {

        setTitle("SportShop");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(1200, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel =
                new JPanel(new BorderLayout());

        mainPanel.add(
                createTopPanel(),
                BorderLayout.NORTH
        );

        mainPanel.add(
                createImagePanel(),
                BorderLayout.CENTER
        );

        setContentPane(mainPanel);
    }


    /*
     * Erstellt den oberen Bereich.
     */
    private JPanel createTopPanel() {

        JPanel topPanel =
                new JPanel(new BorderLayout());

        topPanel.setBackground(Color.BLACK);

        topPanel.setPreferredSize(
                new Dimension(
                        1200,
                        TOP_PANEL_HEIGHT
                )
        );

        topPanel.setBorder(
                new EmptyBorder(
                        10,
                        20,
                        10,
                        20
                )
        );

        /*
         * Der Warenkorb wird oben rechts eingefügt.
         */
        if (SHOW_CART_BUTTON) {

            topPanel.add(
                    createCartArea(),
                    BorderLayout.EAST
            );
        }

        return topPanel;
    }


    /*
     * Erstellt den Warenkorbbereich oben rechts.
     */
    private JPanel createCartArea() {

        JPanel cartPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                0,
                                0
                        )
                );

        cartPanel.setBackground(
                Color.decode("#2C044F")
        );

        JButton cartButton =
                new JButton("Warenkorb");

        ImageIcon cartIcon =
                loadCartIcon();

        if (cartIcon != null) {
            cartButton.setIcon(cartIcon);
        }

        /*
         * Der Text steht links
         * und das Bild rechts.
         */
        cartButton.setHorizontalTextPosition(
                SwingConstants.LEFT
        );

        cartButton.setVerticalTextPosition(
                SwingConstants.CENTER
        );

        cartButton.setIconTextGap(8);

        cartButton.setPreferredSize(
                new Dimension(
                        180,
                        55
                )
        );

        cartButton.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        15
                )
        );

        cartButton.setForeground(Color.WHITE);

        /*
         * Der Button bleibt durchsichtig.
         * Dadurch sieht man den lila Hintergrund.
         */
        cartButton.setBorderPainted(false);
        cartButton.setFocusPainted(false);
        cartButton.setContentAreaFilled(false);
        cartButton.setOpaque(false);

        cartButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        cartButton.setToolTipText(
                "Warenkorb öffnen"
        );

        cartButton.addActionListener(
                event -> openCartFrame()
        );

        cartPanel.add(cartButton);

        return cartPanel;
    }


    /*
     * Öffnet den Warenkorb.
     */
    private void openCartFrame() {



        /*
         * Ein neues Warenkorbfenster wird nur erstellt,
         * wenn noch keines geöffnet ist.
         */
        if (cartFrame == null
                || !cartFrame.isDisplayable()) {

            cartFrame =
                    new CartFrame(
                            cartService,
                            orderService,
                            invoiceService,
                            currentUser
                    );

            cartFrame.setLocationRelativeTo(this);
        }

        /*
         * Der Inhalt wird vor dem Anzeigen aktualisiert.
         */
        cartFrame.refreshCartView();

        cartFrame.setVisible(true);
        cartFrame.toFront();
        cartFrame.requestFocus();
    }


    /*
     * Lädt das Warenkorb-Bild
     * und verkleinert es auf 42 x 42 Pixel.
     */
    private ImageIcon loadCartIcon() {

        ImageIcon originalIcon = null;

        /*
         * Zuerst sucht Java das Bild
         * über den Ressourcenpfad.
         */
        URL iconUrl =
                StartFrame.class.getResource(
                        CART_ICON_PATH
                );

        if (iconUrl != null) {

            originalIcon =
                    new ImageIcon(iconUrl);
        }

        /*
         * Falls der Ressourcenpfad nicht funktioniert,
         * werden normale Dateipfade geprüft.
         */
        if (originalIcon == null) {

            String[] possiblePaths = {
                    "src/ui/images/cart-symbol.png",
                    "SportShop-Grundlage/src/ui/images/cart-symbol.png"
            };

            for (String path : possiblePaths) {

                File iconFile =
                        new File(path);

                if (iconFile.exists()) {

                    originalIcon =
                            new ImageIcon(path);

                    break;
                }
            }
        }

        /*
         * Ohne gefundenes Bild wird kein Icon angezeigt.
         */
        if (originalIcon == null) {
            return null;
        }

        /*
         * Das große Bild wird auf die passende
         * Größe für den Button verkleinert.
         */
        Image scaledImage =
                originalIcon
                        .getImage()
                        .getScaledInstance(
                                42,
                                42,
                                Image.SCALE_SMOOTH
                        );

        return new ImageIcon(scaledImage);
    }


    /*
     * Erstellt den Bereich mit dem Hintergrundbild.
     */
    private JPanel createImagePanel() {

        Image backgroundImage =
                loadBackgroundImage();

        BackgroundPanel imagePanel =
                new BackgroundPanel(
                        backgroundImage
                );

        imagePanel.setLayout(
                new BorderLayout()
        );

        imagePanel.add(
                createButtonContentPanel(),
                BorderLayout.CENTER
        );

        return imagePanel;
    }


    /*
     * Erstellt die Texte und Buttons
     * innerhalb des Bildbereichs.
     */
    private JPanel createButtonContentPanel() {

        JPanel contentPanel =
                new JPanel();

        /*
         * Mit null können die Komponenten
         * einzeln über setBounds positioniert werden.
         */
        contentPanel.setLayout(null);

        /*
         * Das Panel bleibt durchsichtig,
         * damit das Hintergrundbild sichtbar ist.
         */
        contentPanel.setOpaque(false);


        /*
         * Große Überschrift.
         */
        JLabel welcomeLabel =
                new JLabel(
                        "WILLKOMMEN IM SPORTSHOP",
                        SwingConstants.CENTER
                );

        welcomeLabel.setForeground(Color.WHITE);

        welcomeLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        32
                )
        );

        welcomeLabel.setBounds(
                300,
                10,
                600,
                45
        );


        /*
         * Kleiner Text unter der Überschrift.
         */
        JLabel textLabel =
                new JLabel(
                        "Entdecke Sportmode nach deinem Geschmack",
                        SwingConstants.CENTER
                );

        textLabel.setForeground(Color.WHITE);

        textLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        17
                )
        );

        textLabel.setBounds(
                250,
                50,
                700,
                30
        );


        /*
         * Jetzt-shoppen-Button.
         */
        JButton shopButton =
                createButton(
                        "Jetzt shoppen",
                        200,
                        46
                );


        shopButton.setBounds(
                500,
                525,
                200,
                46
        );

        shopButton.addActionListener(
                event -> openProductList()
        );


        /*
         * Mein-Konto-Button.
         */
        JButton profileButton =
                createButton(
                        "Mein Konto",
                        145,
                        42
                );

        profileButton.setBounds(
                345,
                535,
                145,
                42
        );

        profileButton.addActionListener(
                event -> openProfileFrame()
        );


        /*
         * Ausloggen-Button.
         */
        JButton logoutButton =
                createButton(
                        "Ausloggen",
                        145,
                        42
                );

        logoutButton.setBounds(
                710,
                535,
                145,
                42
        );

        logoutButton.addActionListener(
                event -> logout()
        );


        contentPanel.add(welcomeLabel);
        contentPanel.add(textLabel);
        contentPanel.add(shopButton);
        contentPanel.add(profileButton);
        contentPanel.add(logoutButton);

        return contentPanel;
    }


    /*
     * Öffnet das Profil des eingeloggten Benutzers.
     */
    private void openProfileFrame() {

        ProfileFrame profileFrame =
                new ProfileFrame(
                        userService,
                        currentUser
                );

        profileFrame.setLocationRelativeTo(this);
        profileFrame.setVisible(true);
    }


    /*
     * Meldet den Benutzer ab.
     */
    private void logout() {

        int answer =
                JOptionPane.showConfirmDialog(
                        this,
                        "Möchten Sie sich wirklich ausloggen?",
                        "Ausloggen",
                        JOptionPane.YES_NO_OPTION
                );

        /*
         * Bei Nein bleibt das Startfenster geöffnet.
         */
        if (answer != JOptionPane.YES_OPTION) {
            return;
        }

        /*
         * Ein geöffnetes Warenkorbfenster
         * wird ebenfalls geschlossen.
         */
        if (cartFrame != null
                && cartFrame.isDisplayable()) {

            cartFrame.dispose();
        }

        /*
         * Der Warenkorb wird hier nicht gelöscht.
         *
         * Er wird über die Benutzer-ID gespeichert
         * und soll nach dem nächsten Login
         * weiterhin vorhanden sein.
         */
        LoginFrame loginFrame =
                new LoginFrame(userService);

        loginFrame.setLocationRelativeTo(this);
        loginFrame.setVisible(true);

        dispose();
    }


    /*
     * Erstellt einen einheitlichen Button.
     */
    private JButton createButton(
            String text,
            int width,
            int height) {

        JButton button =
                new JButton(text);

        Dimension buttonSize =
                new Dimension(
                        width,
                        height
                );

        button.setPreferredSize(buttonSize);
        button.setMaximumSize(buttonSize);

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        15
                )
        );

        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);

        button.setFocusPainted(false);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }


    /*
     * Zeigt eine Platzhalter-Meldung.
     */
    private void showPlaceholderMessage(
            String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Noch nicht verbunden",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    /*
     * Lädt das Hintergrundbild.
     */
    private Image loadBackgroundImage() {

        URL imageUrl =
                StartFrame.class.getResource(
                        BACKGROUND_IMAGE_PATH
                );

        /*
         * Zuerst versucht Java,
         * das Bild über den Klassenpfad zu laden.
         */
        if (imageUrl != null) {

            return new ImageIcon(
                    imageUrl
            ).getImage();
        }

        /*
         * Falls der Klassenpfad nicht funktioniert,
         * werden normale Dateipfade geprüft.
         */
        String[] possiblePaths = {
                "src/ui/images/nike-wm.png",
                "SportShop-Grundlage/src/ui/images/nike-wm.png"
        };

        for (String path : possiblePaths) {

            File imageFile =
                    new File(path);

            if (imageFile.exists()) {

                return new ImageIcon(
                        path
                ).getImage();
            }
        }

        JOptionPane.showMessageDialog(
                this,
                "Das Hintergrundbild wurde nicht gefunden.\n"
                        + "Erwarteter Ort:\n"
                        + "src/ui/images/nike-wm.png",
                "Bild fehlt",
                JOptionPane.ERROR_MESSAGE
        );

        return null;
    }


    /*
     * Zeichnet das Hintergrundbild.
     */
    private static class BackgroundPanel extends JPanel {

        private final Image backgroundImage;


        public BackgroundPanel(
                Image backgroundImage) {

            this.backgroundImage =
                    backgroundImage;

            setBackground(Color.WHITE);
            setOpaque(true);
        }


        @Override
        protected void paintComponent(
                Graphics graphics) {

            super.paintComponent(graphics);

            if (backgroundImage == null) {
                return;
            }

            Graphics2D graphics2D =
                    (Graphics2D) graphics.create();

            graphics2D.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR
            );

            /*
             * Das Bild füllt den gesamten Bildbereich.
             * Ein zusätzlicher weißer Verlauf wird nicht gezeichnet.
             */
            graphics2D.drawImage(
                    backgroundImage,
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    this
            );

            graphics2D.dispose();
        }
    }

    private void openProductList() {

        ProductListFrame frame =
                new ProductListFrame(
                        productService,
                        cartService
                );

        frame.setLocationRelativeTo(this);
        frame.setVisible(true);
    }
}