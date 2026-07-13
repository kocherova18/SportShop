import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Address;
import model.CartItem;
import model.Order;
import model.Product;
import model.User;
import service.InvoiceService;

public class InvoiceServiceTest {

    public static void main(String[] args) {
        InvoiceService invoiceService =
                new InvoiceService();

        Address address = new Address(
                "Musterstraße",
                "5",
                "10115",
                "Berlin",
                "Deutschland"
        );

        User customer = new User(
                1,
                "Anna",
                "anna@test.de",
                "passwort",
                address,
                User.ROLE_CUSTOMER
        );

        Product shoes = new Product(
                1,
                "Laufschuhe",
                "Schuhe zum Joggen",
                50.0,
                "Schuhe",
                ""
        );

        Product shirt = new Product(
                2,
                "Sportshirt",
                "Leichtes Sportshirt",
                20.0,
                "Kleidung",
                ""
        );

        List<CartItem> items = new ArrayList<>();

        items.add(new CartItem(shoes, 2));
        items.add(new CartItem(shirt, 1));

        Order order = new Order(
                9999,
                customer,
                items,
                Order.STATUS_CREATED,
                LocalDateTime.now()
        );

        String invoiceText =
                invoiceService.createInvoiceText(order);

        System.out.println(invoiceText);

        boolean invoiceSaved =
                invoiceService.saveInvoiceAsText(order);

        System.out.println(
                "Rechnung gespeichert: " + invoiceSaved
        );
    }
}
