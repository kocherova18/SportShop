package service;

import model.CartItem;
import model.Order;
import model.User;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class InvoiceService {

    private static final String INVOICE_DIRECTORY = "data/invoices";

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public String createInvoiceText(Order order) {
        if (!isOrderValid(order)) {
            return null;
        }

        StringBuilder invoice = new StringBuilder();
        User customer = order.getCustomer();

        invoice.append("SPORTSHOP - RECHNUNG\n");
        invoice.append("======================\n\n");

        invoice.append("Rechnungsnummer: ")
                .append(order.getOrderId())
                .append("\n");

        invoice.append("Bestelldatum: ")
                .append(order.getOrderDate().format(DATE_FORMAT))
                .append("\n");

        invoice.append("Status: ")
                .append(order.getStatus())
                .append("\n\n");

        invoice.append("Kunde:\n");
        invoice.append(customer.getName()).append("\n");
        invoice.append(customer.getEmail()).append("\n");

        if (customer.getAddress() != null) {
            invoice.append(customer.getAddress()).append("\n");
        } else {
            invoice.append("Keine Adresse hinterlegt\n");
        }

        invoice.append("\nArtikel:\n");
        invoice.append("----------------------\n");

        for (CartItem item : order.getItems()) {
            invoice.append(item.getQuantity())
                    .append(" x ")
                    .append(item.getProduct().getName())
                    .append("\n");

            invoice.append("Einzelpreis: ")
                    .append(formatPrice(item.getProduct().getPrice()))
                    .append("\n");

            invoice.append("Zwischensumme: ")
                    .append(formatPrice(item.getSubtotal()))
                    .append("\n\n");
        }

        invoice.append("----------------------\n");

        invoice.append("Gesamtbetrag: ")
                .append(formatPrice(order.getTotalPrice()))
                .append("\n");

        return invoice.toString();
    }

    public boolean saveInvoiceAsText(Order order) {
        String invoiceText = createInvoiceText(order);

        if (invoiceText == null) {
            return false;
        }

        try {
            Path directory = Paths.get(INVOICE_DIRECTORY);

            // Der Rechnungsordner wird erstellt, falls er noch nicht existiert.
            Files.createDirectories(directory);

            Path invoiceFile = directory.resolve(
                    "Rechnung_" + order.getOrderId() + ".txt"
            );

            Files.write(
                    invoiceFile,
                    invoiceText.getBytes(StandardCharsets.UTF_8)
            );

            return true;
        } catch (IOException e) {
            System.err.println(
                    "Rechnung konnte nicht gespeichert werden: "
                            + e.getMessage()
            );

            return false;
        }
    }

    private boolean isOrderValid(Order order) {
        if (order == null
                || order.getCustomer() == null
                || order.getOrderDate() == null
                || order.getItems() == null
                || order.getItems().isEmpty()) {
            return false;
        }

        List<CartItem> items = order.getItems();

        for (CartItem item : items) {
            if (item == null
                    || item.getProduct() == null
                    || item.getQuantity() <= 0) {
                return false;
            }
        }

        return true;
    }

    private String formatPrice(double price) {
        return String.format(
                Locale.GERMANY,
                "%.2f EUR",
                price
        );
    }
}
