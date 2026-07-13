package ui;

import model.CartItem;
import model.Order;
import model.User;
import service.CartService;
import service.InvoiceService;
import service.OrderService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartFrame extends JFrame {

    private CartService cartService;
    private OrderService orderService;
    private InvoiceService invoiceService;
    private User currentUser;

    private JTable cartTable;
    private DefaultTableModel tableModel;
    private JLabel totalPriceLabel;
    private JButton changeQuantityButton;
    private JButton removeButton;
    private JButton orderButton;

    // Diese Liste verbindet die Tabellenzeilen mit den echten Warenkorbpositionen.
    private List<CartItem> displayedItems;

    public CartFrame(
            CartService cartService,
            OrderService orderService,
            InvoiceService invoiceService,
            User currentUser) {

        if (cartService == null
                || orderService == null
                || invoiceService == null
                || currentUser == null) {
            throw new IllegalArgumentException(
                    "CartFrame benötigt alle Services und einen Benutzer."
            );
        }

        // Die vorhandenen Services werden übernommen, damit alle Fenster
        // mit demselben Warenkorb und denselben Bestellungen arbeiten.
        this.cartService = cartService;
        this.orderService = orderService;
        this.invoiceService = invoiceService;
        this.currentUser = currentUser;
        this.displayedItems = new ArrayList<>();

        initializeUI();
        refreshCartView();
    }

    private void initializeUI() {
        setTitle("Warenkorb");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(760, 460);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        setContentPane(mainPanel);

        JLabel titleLabel = new JLabel("Warenkorb");
        titleLabel.setFont(
                titleLabel.getFont().deriveFont(Font.BOLD, 20f)
        );

        mainPanel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {
                "Produkt",
                "Einzelpreis",
                "Menge",
                "Zwischensumme"
        };

        // Die Tabelle dient nur zur Anzeige.
        // Änderungen laufen über die Buttons und den CartService.
        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        cartTable = new JTable(tableModel);
        cartTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        cartTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane tableScrollPane =
                new JScrollPane(cartTable);

        mainPanel.add(
                tableScrollPane,
                BorderLayout.CENTER
        );

        JPanel bottomPanel =
                new JPanel(new BorderLayout(10, 10));

        totalPriceLabel =
                new JLabel("Gesamtpreis: 0,00 EUR");

        totalPriceLabel.setFont(
                totalPriceLabel.getFont()
                        .deriveFont(Font.BOLD, 15f)
        );

        bottomPanel.add(
                totalPriceLabel,
                BorderLayout.NORTH
        );

        JPanel buttonPanel =
                new JPanel(new FlowLayout(FlowLayout.RIGHT));

        changeQuantityButton =
                new JButton("Menge ändern");

        removeButton =
                new JButton("Entfernen");

        orderButton =
                new JButton("Bestellen");

        changeQuantityButton.addActionListener(
                event -> changeQuantity()
        );

        removeButton.addActionListener(
                event -> removeSelectedItem()
        );

        orderButton.addActionListener(
                event -> placeOrder()
        );

        buttonPanel.add(changeQuantityButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(orderButton);

        bottomPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );
    }

    public void refreshCartView() {
        // Die Anzeige wird immer neu aus dem CartService aufgebaut.
        displayedItems.clear();
        displayedItems.addAll(
                cartService.getCartItems()
        );

        tableModel.setRowCount(0);

        for (CartItem item : displayedItems) {
            Object[] row = {
                    item.getProduct().getName(),
                    formatPrice(
                            item.getProduct().getPrice()
                    ),
                    item.getQuantity(),
                    formatPrice(
                            item.getSubtotal()
                    )
            };

            tableModel.addRow(row);
        }

        totalPriceLabel.setText(
                "Gesamtpreis: "
                        + formatPrice(
                                cartService.calculateTotalPrice()
                        )
        );

        boolean cartHasItems =
                !displayedItems.isEmpty();

        changeQuantityButton.setEnabled(
                cartHasItems
        );

        removeButton.setEnabled(
                cartHasItems
        );

        orderButton.setEnabled(
                cartHasItems
        );
    }

    private CartItem getSelectedCartItem() {
        int selectedRow =
                cartTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Bitte zuerst ein Produkt auswählen.",
                    "Keine Auswahl",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        int modelRow =
                cartTable.convertRowIndexToModel(
                        selectedRow
                );

        if (modelRow < 0
                || modelRow >= displayedItems.size()) {
            return null;
        }

        return displayedItems.get(modelRow);
    }

    private void changeQuantity() {
        CartItem selectedItem =
                getSelectedCartItem();

        if (selectedItem == null) {
            return;
        }

        String input =
                JOptionPane.showInputDialog(
                        this,
                        "Neue Menge für "
                                + selectedItem
                                        .getProduct()
                                        .getName()
                                + ":",
                        selectedItem.getQuantity()
                );

        // Bei Abbrechen wird keine Änderung durchgeführt.
        if (input == null) {
            return;
        }

        try {
            int newQuantity =
                    Integer.parseInt(
                            input.trim()
                    );

            if (newQuantity <= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Die Menge muss mindestens 1 sein.",
                        "Ungültige Menge",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            boolean updated =
                    cartService.updateQuantity(
                            selectedItem
                                    .getProduct()
                                    .getId(),
                            newQuantity
                    );

            if (updated) {
                refreshCartView();
            } else {
                showError(
                        "Die Menge konnte nicht geändert werden."
                );
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Bitte eine ganze Zahl eingeben.",
                    "Ungültige Eingabe",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void removeSelectedItem() {
        CartItem selectedItem =
                getSelectedCartItem();

        if (selectedItem == null) {
            return;
        }

        int answer =
                JOptionPane.showConfirmDialog(
                        this,
                        "Soll "
                                + selectedItem
                                        .getProduct()
                                        .getName()
                                + " aus dem Warenkorb entfernt werden?",
                        "Produkt entfernen",
                        JOptionPane.YES_NO_OPTION
                );

        if (answer != JOptionPane.YES_OPTION) {
            return;
        }

        boolean removed =
                cartService.removeFromCart(
                        selectedItem
                                .getProduct()
                                .getId()
                );

        if (removed) {
            refreshCartView();
        } else {
            showError(
                    "Das Produkt konnte nicht entfernt werden."
            );
        }
    }

    private void placeOrder() {
        if (cartService.isEmpty()) {
            showError("Der Warenkorb ist leer.");
            return;
        }

        int answer =
                JOptionPane.showConfirmDialog(
                        this,
                        "Bestellung für "
                                + formatPrice(
                                        cartService
                                                .calculateTotalPrice()
                                )
                                + " abschließen?",
                        "Bestellung bestätigen",
                        JOptionPane.YES_NO_OPTION
                );

        if (answer != JOptionPane.YES_OPTION) {
            return;
        }

        Order newOrder =
                orderService.createOrder(
                        currentUser,
                        cartService.getCartItems()
                );

        if (newOrder == null) {
            showError(
                    "Die Bestellung konnte nicht erstellt werden."
            );

            return;
        }

        String invoiceText =
                invoiceService.createInvoiceText(
                        newOrder
                );

        boolean invoiceSaved =
                invoiceService.saveInvoiceAsText(
                        newOrder
                );

        // Die Bestellung wurde bereits erstellt.
        // Deshalb wird der Warenkorb auch dann geleert,
        // wenn nur das Speichern der Rechnung fehlschlägt.
        cartService.clearCart();
        refreshCartView();

        if (invoiceText != null) {
            showInvoice(
                    invoiceText,
                    newOrder.getOrderId()
            );
        }

        if (invoiceSaved) {
            JOptionPane.showMessageDialog(
                    this,
                    "Bestellung #"
                            + newOrder.getOrderId()
                            + " wurde erstellt.\n"
                            + "Die Rechnung wurde gespeichert.",
                    "Bestellung erfolgreich",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Bestellung #"
                            + newOrder.getOrderId()
                            + " wurde erstellt.\n"
                            + "Die Rechnung konnte nicht "
                            + "als Datei gespeichert werden.",
                    "Bestellung erstellt",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void showInvoice(
            String invoiceText,
            int orderId) {

        JTextArea invoiceArea =
                new JTextArea(invoiceText);

        invoiceArea.setEditable(false);
        invoiceArea.setFont(
                new Font(
                        Font.MONOSPACED,
                        Font.PLAIN,
                        12
                )
        );

        invoiceArea.setRows(20);
        invoiceArea.setColumns(45);
        invoiceArea.setCaretPosition(0);

        JScrollPane scrollPane =
                new JScrollPane(invoiceArea);

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Rechnung für Bestellung #" + orderId,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Fehler",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private String formatPrice(double price) {
        return String.format(
                Locale.GERMANY,
                "%.2f EUR",
                price
        );
    }
}
