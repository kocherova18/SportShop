import data.DataManager;
import java.util.ArrayList;
import java.util.List;
import model.Order;
import model.Product;
import model.User;
import service.CartService;
import service.OrderService;

public class OrderServiceTest {

    public static void main(String[] args) {
        TestDataManager dataManager = new TestDataManager();

        OrderService orderService =
                new OrderService(dataManager);

        CartService cartService =
                new CartService();

        User customer1 = new User(
                1,
                "Anna",
                "anna@test.de",
                "passwort",
                null,
                User.ROLE_CUSTOMER
        );

        User customer2 = new User(
                2,
                "Ben",
                "ben@test.de",
                "passwort",
                null,
                User.ROLE_CUSTOMER
        );

        User admin = new User(
                3,
                "Admin",
                "admin@test.de",
                "passwort",
                null,
                User.ROLE_ADMIN
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

        Order newOrder = orderService.createOrder(
                customer1,
                cartService.getCartItems()
        );

        System.out.println("Bestellung erstellt: "
                + (newOrder != null));

        if (newOrder != null) {
            System.out.println("Bestellnummer: "
                    + newOrder.getOrderId());

            System.out.println("Kunde: "
                    + newOrder.getCustomer().getName());

            System.out.println("Gesamtpreis: "
                    + newOrder.getTotalPrice());

            System.out.println("Status: "
                    + newOrder.getStatus());

            cartService.clearCart();

            System.out.println("Warenkorb danach leer: "
                    + cartService.isEmpty());

            System.out.println("Bestellung hat weiterhin Positionen: "
                    + newOrder.getItems().size());
        }

        System.out.println("Bestellungen von Anna: "
                + orderService.getOrdersForUser(customer1).size());

        System.out.println("Bestellungen von Ben: "
                + orderService.getOrdersForUser(customer2).size());

        Order foreignOrder = orderService.getOrderForUser(
                customer2,
                newOrder.getOrderId()
        );

        System.out.println("Ben kann Annas Bestellung öffnen: "
                + (foreignOrder != null));

        System.out.println("Anna sieht alle Bestellungen: "
                + orderService.getAllOrders(customer1).size());

        System.out.println("Admin sieht alle Bestellungen: "
                + orderService.getAllOrders(admin).size());

        boolean customerChangedStatus =
                orderService.updateOrderStatus(
                        customer1,
                        newOrder.getOrderId(),
                        Order.STATUS_PAID
                );

        System.out.println("Anna kann Status ändern: "
                + customerChangedStatus);

        boolean adminChangedStatus =
                orderService.updateOrderStatus(
                        admin,
                        newOrder.getOrderId(),
                        Order.STATUS_PAID
                );

        System.out.println("Admin kann Status ändern: "
                + adminChangedStatus);

        System.out.println("Neuer Status: "
                + newOrder.getStatus());

        System.out.println("Gespeicherte Bestellungen: "
                + dataManager.getSavedOrdersCount());
    }

    // Dieser DataManager speichert nur für den Test im Arbeitsspeicher.
    private static class TestDataManager extends DataManager {

        private List<Order> testOrders = new ArrayList<>();

        @Override
        public List<Order> loadOrders() {
            return new ArrayList<>(testOrders);
        }

        @Override
        public void saveOrders(List<Order> orders) {
            testOrders = new ArrayList<>(orders);
        }

        public int getSavedOrdersCount() {
            return testOrders.size();
        }
    }
}
