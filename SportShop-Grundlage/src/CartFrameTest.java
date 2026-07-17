import data.DataManager;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import model.Address;
import model.Order;
import model.Product;
import model.User;
import service.CartService;
import service.InvoiceService;
import service.OrderService;
import ui.CartFrame;
import service.UserService;

public class CartFrameTest {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TestDataManager dataManager =
                    new TestDataManager();

            CartService cartService =
                    new CartService(dataManager, 1);

            OrderService orderService =
                    new OrderService(dataManager);

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

            cartService.addToCart(shoes, 2);
            cartService.addToCart(shirt, 1);

            CartFrame cartFrame =
                    new CartFrame(
                            cartService,
                            orderService,
                            invoiceService,
                            customer
                    );

            cartFrame.setVisible(true);
        });
    }

    // Für den UI-Test werden Bestellungen
    // nur im Arbeitsspeicher gespeichert.
    private static class TestDataManager
            extends DataManager {

        private List<Order> testOrders =
                new ArrayList<>();

        @Override
        public List<Order> loadOrders() {
            return new ArrayList<>(testOrders);
        }

        @Override
        public void saveOrders(
                List<Order> orders) {

            testOrders =
                    new ArrayList<>(orders);
        }
    }
}
