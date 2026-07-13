import model.CartItem;
import model.Product;
import service.CartService;

public class CartServiceTest {

    public static void main(String[] args) {
        CartService cartService = new CartService();

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

        System.out.println("Warenkorb am Anfang leer: "
                + cartService.isEmpty());

        cartService.addToCart(shoes, 2);
        cartService.addToCart(shirt, 1);

        System.out.println("Anzahl der Positionen: "
                + cartService.getCartItems().size());

        System.out.println("Gesamtpreis: "
                + cartService.calculateTotalPrice());

        // Dasselbe Produkt wird noch einmal hinzugefügt.
        cartService.addToCart(shoes, 3);

        System.out.println("Warenkorb nach erneutem Hinzufügen:");

        for (CartItem item : cartService.getCartItems()) {
            System.out.println(item);
        }

        cartService.updateQuantity(shirt.getId(), 4);

        System.out.println("Gesamtpreis nach Mengenänderung: "
                + cartService.calculateTotalPrice());

        cartService.removeFromCart(shoes.getId());

        System.out.println("Anzahl nach Entfernen: "
                + cartService.getCartItems().size());

        cartService.clearCart();

        System.out.println("Warenkorb am Ende leer: "
                + cartService.isEmpty());
    }
}
