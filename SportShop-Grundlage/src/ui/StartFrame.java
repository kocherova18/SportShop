package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.net.URL;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
            "/ui/images/nike-wm.jpg";

    private static final String CART_ICON_PATH =
            "/ui/images/cart-symbol.jpg";

    private static final int TOP_PANEL_HEIGHT = 70;

    // Höhe des unteren weißen Bereichs
    private static final int BOTTOM_PANEL_HEIGHT = 120;

    private static final boolean SHOW_CART_BUTTON = true;

    private final UserService userService;

    private final CartService cartService;
    private final OrderService orderService;
    private final InvoiceService invoiceService;
    private final User currentUser;

    private CartFrame cartFrame;

    public StartFrame(
            UserService userService,
            CartService cartService,
            OrderService orderService,
            InvoiceService invoiceService,
            User currentUser) {

        if (userService == null
                ||cartService == null
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

        /*
         * Das Hauptpanel besteht aus:
         *
         * NORTH  = oberer weißer Bereich
         * CENTER = Hintergrundbild mit Buttons
         * SOUTH  = unterer weißer Bereich
         */
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

        mainPanel.add(
                createBottomPanel(),
                BorderLayout.SOUTH
        );

        setContentPane(mainPanel);
    }

    /*
     * Erstellt den oberen weißen Bereich.
     */
    private JPanel createTopPanel() {

        JPanel topPanel =
                new JPanel(new BorderLayout());

        topPanel.setBackground(Color.WHITE);

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
         * Dieses leere Panel ist für spätere Elemente gedacht.
         *
         * Hier könnten später zum Beispiel stehen:
         * Startseite, Produkte, Profil oder Suche.
         */


        /*
         * Der Warenkorb ist getrennt aufgebaut.
         * Er kann deshalb später einfach entfernt werden.
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
     * Erstellt nur den Bereich für den Warenkorb.
     */
/*
 * Erstellt den getrennten Warenkorbbereich
 * oben rechts.
 */
/*
 * Erstellt den Warenkorbbereich oben rechts.
 */
/*
 * Erstellt den Warenkorb oben rechts.
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

    cartPanel.setBackground(Color.WHITE);

    JButton cartButton =
            new JButton("Warenkorb");

    ImageIcon cartIcon =
            loadCartIcon();

    if (cartIcon != null) {
        cartButton.setIcon(cartIcon);
    }

    // Text links, Bild rechts
    cartButton.setHorizontalTextPosition(
            SwingConstants.LEFT
    );

    cartButton.setVerticalTextPosition(
            SwingConstants.CENTER
    );

    cartButton.setIconTextGap(8);

    cartButton.setPreferredSize(
            new Dimension(180, 55)
    );

    cartButton.setFont(
            new Font(
                    "SansSerif",
                    Font.BOLD,
                    15
            )
    );

    cartButton.setForeground(Color.BLACK);
    cartButton.setBorderPainted(false);
    cartButton.setFocusPainted(false);
    cartButton.setContentAreaFilled(false);

    cartButton.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
    );

    cartButton.setToolTipText(
            "Warenkorb öffnen"
    );

    cartButton.addActionListener(event ->
            openCartFrame()
    );

    cartPanel.add(cartButton);

    return cartPanel;
}

/*
 * Öffnet den vorhandenen CartFrame.
 */
private void openCartFrame() {

    /*
     * Wenn noch kein Warenkorbfenster existiert
     * oder das alte Fenster geschlossen wurde,
     * wird ein neues erstellt.
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
     * Die Tabelle wird vor dem Anzeigen
     * erneut aus dem CartService aufgebaut.
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
     * Falls das Laden über den Ressourcenpfad
     * nicht funktioniert, werden normale
     * Dateipfade geprüft.
     */
    if (originalIcon == null) {

        String[] possiblePaths = {
                "src/ui/images/cart-symbol.jpg",
                "SportShop-Grundlage/src/ui/images/cart-symbol.jpg"
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
     * Wenn das Bild nicht gefunden wurde,
     * wird null zurückgegeben.
     */
    if (originalIcon == null) {
        return null;
    }

    /*
     * Das sehr große Originalbild
     * wird auf 42 x 42 Pixel verkleinert.
     */
    Image scaledImage =
            originalIcon
                    .getImage()
                    .getScaledInstance(
                            42,
                            42,
                            Image.SCALE_SMOOTH
                    );

    return new ImageIcon(
            scaledImage
    );
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

        /*
         * Die drei Buttons werden innerhalb
         * des Bildbereichs eingefügt.
         */
        imagePanel.add(
                createButtonContentPanel(),
                BorderLayout.CENTER
        );

        return imagePanel;
    }

    /*
     * Erstellt den Text und die Button-Pyramide.
     */
/*
 * Erstellt Text und Buttons im Bildbereich.
 *
 * Durch setBounds können die Komponenten
 * unabhängig voneinander positioniert werden.
 */
        private JPanel createButtonContentPanel() {

        JPanel contentPanel = new JPanel();

        /*
        * null bedeutet:
        * Wir bestimmen die Positionen selbst mit setBounds.
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

        /*
        * Position der großen Überschrift:
        *
        * x = 300
        * y = 70
        * Breite = 600
        * Höhe = 45
        */
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

        /*
        * Position des kleinen Textes.
        */
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

        /*
        * Position des Jetzt-shoppen-Buttons.
        */
        shopButton.setBounds(
                500,
                400,
                200,
                46
        );

        shopButton.addActionListener(event ->
                showPlaceholderMessage(
                        "Das Home- oder Produktfenster "
                                + "wird später geöffnet."
                )
        );

        /*
        * Panel mit Anmelden und Registrieren.
        */
        JPanel accountButtonPanel =
                createAccountButtonPanel();

        /*
        * Position der unteren beiden Buttons.
        */
        accountButtonPanel.setBounds(
                425,
                350,
                350,
                55
        );

        contentPanel.add(welcomeLabel);
        contentPanel.add(textLabel);
        contentPanel.add(shopButton);
        contentPanel.add(accountButtonPanel);

        return contentPanel;
        }

    /*
     * Erstellt Anmeldung und Registrierung.
     */
    private JPanel createAccountButtonPanel() {

        JPanel accountPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                16,
                                0
                        )
                );

        accountPanel.setOpaque(false);

        JButton profileButton =
                createButton(
                        "Mein Konto",
                        145,
                        42
                );

        profileButton.addActionListener(event -> openProfileFrame());

        JButton logoutButton =
                createButton(
                        "Ausloggen",
                        145,
                        42
                );

        logoutButton.addActionListener(event -> logout());

        accountPanel.add(profileButton);
        accountPanel.add(logoutButton);

        return accountPanel;
    }

    private void openProfileFrame() {
        ProfileFrame profileFrame =
                new ProfileFrame(
                        userService,
                        currentUser
                );

        profileFrame.setLocationRelativeTo(this);
        profileFrame.setVisible(true);
    }

    private void logout() {
        int answer =
                JOptionPane.showConfirmDialog(
                        this,
                        "Möchten Sie sich wirklich ausloggen?",
                        "Ausloggen",
                        JOptionPane.YES_NO_OPTION
                );

        if (answer != JOptionPane.YES_OPTION) {
            return;
        }

        if (cartFrame != null) {
            cartFrame.dispose();
        }

        cartService.clearCart();

        LoginFrame loginFrame =
                new LoginFrame(userService);

        loginFrame.setVisible(true);

        dispose();
    }

    /*
     * Erstellt den unteren weißen Bereich.
     */
    private JPanel createBottomPanel() {

        JPanel bottomPanel =
                new JPanel();

        bottomPanel.setBackground(Color.WHITE);

        /*
         * Die Höhe beträgt:
         *
         * 70 Pixel mal 3 = 210 Pixel
         */
        bottomPanel.setPreferredSize(
                new Dimension(
                        1200,
                        BOTTOM_PANEL_HEIGHT
                )
        );

        bottomPanel.setLayout(
                new BoxLayout(
                        bottomPanel,
                        BoxLayout.Y_AXIS
                )
        );

        /*
         * SPORT und SHOP sind getrennte Labels.
         *
         * Dadurch können beide Wörter
         * unterschiedliche Farben haben.
         */
        JPanel brandPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                0,
                                0
                        )
                );

        brandPanel.setBackground(Color.WHITE);

        brandPanel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel sportLabel =
                new JLabel("Sport");

        sportLabel.setForeground(Color.BLACK);

        sportLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        42
                )
        );

        JLabel shopLabel =
                new JLabel("Shop");

        /*
         * Lila Farbe:
         *
         * Rot   = 128
         * Grün  = 0
         * Blau  = 180
         */
        shopLabel.setForeground(
                new Color(
                        128,
                        0,
                        180
                )
        );

        shopLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        42
                )
        );

        brandPanel.add(sportLabel);
        brandPanel.add(shopLabel);

        /*
         * Der Schriftzug wird vertikal und
         * horizontal zentriert.
         */
        bottomPanel.add(
                Box.createVerticalGlue()
        );

        bottomPanel.add(brandPanel);

        bottomPanel.add(
                Box.createVerticalGlue()
        );

        return bottomPanel;
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
     * Zeigt die Platzhalter-Meldungen.
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
         * Falls das nicht funktioniert,
         * werden diese normalen Pfade geprüft.
         */
        String[] possiblePaths = {
                "src/ui/images/nike-wm.jpg",
                "SportShop-Grundlage/src/ui/images/nike-wm.jpg"
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
                        + "src/ui/images/nike-wm.jpg",
                "Bild fehlt",
                JOptionPane.ERROR_MESSAGE
        );

        return null;
    }

    /*
     * Dieses Panel zeichnet das Hintergrundbild.
     */
 /*
 * Dieses Panel zeichnet das Hintergrundbild
 * in einer ovalen Form.
 *
 * Der Rand wird langsam weiß,
 * damit das Bild nach außen ausblendet.

 * Dieses Panel zeichnet das Hintergrundbild
 * mit einem weichen Transparenzverlauf.
 *
 * In der Mitte bleibt das Bild sichtbar.
 * Zu den äußeren Bereichen wird es langsam transparent.
 */
/*
 * Zeichnet das Hintergrundbild
 * mit einem weichen Verlauf an den Rändern.
 */
/*
 * Zeichnet das Hintergrundbild.
 * Nur links und rechts wird das Bild
 * langsam weiß ausgeblendet.
 */
private static class BackgroundPanel extends JPanel {

    private final Image backgroundImage;

    // Breite des Verlaufs links und rechts
    private static final int FADE_SIZE = 75;

    // Stärke des weißen Randes
    private static final int EDGE_ALPHA = 180;

    public BackgroundPanel(Image backgroundImage) {
        this.backgroundImage = backgroundImage;

        setBackground(Color.WHITE);
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics graphics) {

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
         * Das vollständige Bild wird normal gezeichnet.
         */
        graphics2D.drawImage(
                backgroundImage,
                0,
                0,
                getWidth(),
                getHeight(),
                this
        );

        Color white =
                new Color(
                        255,
                        255,
                        255,
                        EDGE_ALPHA
                );

        Color transparentWhite =
                new Color(
                        255,
                        255,
                        255,
                        0
                );

        /*
         * Verlauf vom linken Rand in das Bild.
         */
        graphics2D.setPaint(
                new GradientPaint(
                        0,
                        0,
                        white,
                        FADE_SIZE,
                        0,
                        transparentWhite
                )
        );

        graphics2D.fillRect(
                0,
                0,
                FADE_SIZE,
                getHeight()
        );

        /*
         * Verlauf vom rechten Rand in das Bild.
         */
        graphics2D.setPaint(
                new GradientPaint(
                        getWidth(),
                        0,
                        white,
                        getWidth() - FADE_SIZE,
                        0,
                        transparentWhite
                )
        );

        graphics2D.fillRect(
                getWidth() - FADE_SIZE,
                0,
                FADE_SIZE,
                getHeight()
        );

        graphics2D.dispose();
    }
}

}
