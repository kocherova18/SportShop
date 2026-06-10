# SportShop - Projektgrundlage

Dieses Repository ist die gemeinsame Grundlage fuer unseren Online-Shop fuer Sportkleidung.

Ziel: Alle 4 Personen koennen moeglichst unabhaengig arbeiten, ohne sich gegenseitig zu blockieren.

---

## 1. Was ist schon vorbereitet?

Die Grundlage enthaelt:

```text
SportShop/
├── README.md
├── .gitignore
├── data/
│   └── .gitkeep
└── src/
    ├── model/
    │   ├── Address.java
    │   ├── User.java
    │   ├── Product.java
    │   ├── CartItem.java
    │   └── Order.java
    │
    ├── service/
    │   ├── UserService.java
    │   ├── ProductService.java
    │   ├── CartService.java
    │   └── OrderService.java
    │
    ├── data/
    │   └── DataManager.java
    │
    └── ui/
```

---

## 2. Wichtige Regel

Bitte diese gemeinsamen Klassen nicht einfach umbenennen oder Felder loeschen:

```text
User
Address
Product
CartItem
Order
DataManager
UserService
ProductService
CartService
OrderService
```

Wenn jemand eine Model-Klasse aendern muss, vorher kurz in der Gruppe absprechen.

Warum? Diese Klassen werden von mehreren Personen benutzt. Wenn eine Person z. B. `Product` aendert, kann der Code von Warenkorb, Bestellung oder Admin kaputtgehen.

---

## 3. Speicherung mit Serialisierung

Wir verwenden Java-Serialisierung fuer die Speicherung.

Deshalb implementieren alle Model-Klassen:

```java
implements Serializable
```

Betroffene Klassen:

```text
Address.java
User.java
Product.java
CartItem.java
Order.java
```

Die Speicherung laeuft ueber:

```text
DataManager.java
```

Dort sind diese Dateien vorgesehen:

```text
data/users.dat
data/products.dat
data/orders.dat
```

Diese `.dat` Dateien werden automatisch erzeugt, wenn gespeichert wird.

Wichtig: In `.gitignore` steht aktuell:

```text
data/*.dat
```

Das bedeutet: Die gespeicherten lokalen Daten werden nicht automatisch auf GitHub hochgeladen.

Wenn wir spaeter gemeinsame Testdaten teilen wollen, koennen wir diese Zeile entfernen.

---

## 4. Was bedeuten die Model-Klassen?

### User.java

Speichert Kundendaten und Admin-Daten.

Wichtige Felder:

```java
private int id;
private String name;
private String email;
private String passwordHash;
private Address address;
private String role;
```

Wichtig: Es gibt kein Feld `password`, sondern `passwordHash`.

Grund: Passwoerter sollen nicht im Klartext gespeichert werden.

Rollen:

```java
User.ROLE_CUSTOMER
User.ROLE_ADMIN
```

---

### Address.java

Speichert die Lieferadresse.

Wichtige Felder:

```java
private String street;
private String houseNumber;
private String zipCode;
private String city;
private String country;
```

---

### Product.java

Speichert Produktdaten.

Wichtige Felder:

```java
private int id;
private String name;
private String description;
private double price;
private String category;
private String imagePath;
```

Beispiele fuer `category`:

```text
T-Shirt
Hose
Jacke
Schuhe
```

---

### CartItem.java

Ein einzelner Artikel im Warenkorb.

Wichtige Felder:

```java
private Product product;
private int quantity;
```

Beispiel: 2 x Nike Shirt.

---

### Order.java

Speichert eine Bestellung.

Wichtige Felder:

```java
private int orderId;
private User customer;
private List<CartItem> items;
private double totalPrice;
private String status;
private LocalDateTime orderDate;
```

Bestellstatus:

```java
Order.STATUS_CREATED   // ERSTELLT
Order.STATUS_PAID      // BEZAHLT
Order.STATUS_SHIPPED   // VERSENDET
```

---

## 5. Was bedeuten die Service-Klassen?

Die Service-Klassen enthalten die Logik.

Sie sind absichtlich teilweise noch leer und enthalten TODO-Kommentare.

Der Zweck ist: Alle koennen sofort anfangen, weil die Methodennamen schon festgelegt sind.

---

### UserService.java

Fuer Person 1.

Branch: login-profile

Zustaendig fuer:

```text
Registrierung
Login
Passwort aendern
Profil bearbeiten
Lieferadresse speichern
```

Zustaendige Frames:

```text
LoginFrame.java
RegisterFrame.java
ProfileFrame.java
ChangePasswordFrame.java
AddressFrame.java
```

Wichtige Methoden:

```java
register(User user)
login(String email, String password)
changePassword(User user, String oldPassword, String newPassword)
updateProfile(User user, String name, Address address)
```

---

### ProductService.java

Fuer Person 2 und teilweise Person 4.

Branch: products

Zustaendig fuer:

```text
Produktliste
Produktsuche
Produktfilter
Produktdetails
Admin: Produkt hinzufuegen/bearbeiten/loeschen
```

Zustaendige Frames:

```text
ProductListFrame.java
ProductDetailFrame.java
SearchFrame.java
FilterFrame.java
```

Wichtige Methoden:

```java
getAllProducts()
getProductById(int id)
searchProducts(String searchText)
filterProducts(String category, double maxPrice)
addProduct(Product product)
updateProduct(Product product)
deleteProduct(int productId)
```

---

### CartService.java

Fuer Person 3.

Branch: cart-orders

Zustaendig fuer:

```text
Produkt in Warenkorb legen
Produkt aus Warenkorb entfernen
Gesamtpreis berechnen
Warenkorb leeren
```

Zustaendige Frames:

```text
CartFrame.java
CheckoutFrame.java
OrderHistoryFrame.java
OrderDetailFrame.java
```

Wichtige Methoden:

```java
addToCart(Product product, int quantity)
removeFromCart(int productId)
calculateTotalPrice()
getCartItems()
clearCart()
```

---

### OrderService.java

Fuer Person 3 und teilweise Person 4.

Zustaendig fuer:

```text
Bestellung erstellen
Bestellungen eines Kunden anzeigen
Alle Bestellungen anzeigen
Bestellstatus aendern
```

Wichtige Methoden:

```java
createOrder(User customer, List<CartItem> cartItems)
getOrdersByUser(User customer)
getAllOrders()
updateOrderStatus(int orderId, String status)
```

---

## 6. DataManager.java

Fuer Person 4 / Integration.

Branch: admin-data

Zustaendig fuer Serialisierung:

```java
saveUsers(List<User> users)
loadUsers()
saveProducts(List<Product> products)
loadProducts()
saveOrders(List<Order> orders)
loadOrders()
```

Beispiel:

```java
DataManager dataManager = new DataManager();

List<Product> products = dataManager.loadProducts();
products.add(new Product(1, "Sport Shirt", "Leichtes Shirt", 29.99, "T-Shirt", ""));
dataManager.saveProducts(products);
```

Zustaendige Frames:

```text
AdminFrame.java
AdminProductFrame.java
AdminOrderFrame.java
AdminCustomerFrame.java
```

---

## 7. Aufgabenaufteilung

### Person 1: Login, Registrierung, Benutzerprofil

Arbeitet hauptsaechlich mit:

```text
User.java
Address.java
UserService.java
```

Aufgaben:

```text
Registrierung
Login
Passwort aendern
Profil anzeigen/bearbeiten
Lieferadresse speichern/aendern
Zugriff nur auf eigene Daten
```

---

### Person 2: Produkte, Suche, Filter

Arbeitet hauptsaechlich mit:

```text
Product.java
ProductService.java
```

Aufgaben:

```text
Produktliste anzeigen
Produktsuche
Filter nach Kategorie und Preis
Produktdetails anzeigen
```

---

### Person 3: Warenkorb und Bestellung

Arbeitet hauptsaechlich mit:

```text
CartItem.java
CartService.java
Order.java
OrderService.java
Product.java
User.java
```

Aufgaben:

```text
Produkt in Warenkorb legen
Produkt aus Warenkorb entfernen
Gesamtpreis berechnen
Bestellung abschliessen
Bestellhistorie anzeigen
```

Wichtig: Person 3 muss nicht auf Person 2 warten.

Man kann am Anfang Dummy-Produkte verwenden:

```java
Product testProduct = new Product(
    1,
    "Test Shirt",
    "Dummy Produkt fuer Warenkorb-Test",
    29.99,
    "T-Shirt",
    ""
);
```

---

### Person 4: Admin, Speicherung, Integration

Arbeitet hauptsaechlich mit:

```text
DataManager.java
ProductService.java
OrderService.java
UserService.java
```

Aufgaben:

```text
Produkte hinzufuegen/bearbeiten/loeschen
Alle Bestellungen einsehen
Kundendaten verwalten
Admin-Bereich schuetzen
Speichern und Laden mit Serialisierung
Integration der Teile
Tests und Projektstart koordinieren
```

---

## 8. Git-Workflow

### Main Branch

In `main` liegt nur stabiler Code.

In `main` gehoert:

```text
Projektstruktur
gemeinsame Model-Klassen
Service-Grundgerueste
DataManager
README
.gitignore
```

Wichtig: Bitte nicht direkt in `main` arbeiten, wenn ihr etwas neues entwickelt.

---

## 9. Branches fuer jede Person

Jede Person arbeitet in einem eigenen Branch:

```text
login-profile
products
cart-orders
admin-data
```

Beispiel:

```bash
git checkout -b login-profile
```

Dann arbeiten, speichern und committen:

```bash
git add .
git commit -m "Implement login and registration"
git push -u origin login-profile
```

Danach Pull Request nach `main` erstellen.

---

## 10. Wichtig beim parallelen Arbeiten

Damit wir uns nicht blockieren:

1. Nicht alle gleichzeitig dieselbe Datei bearbeiten.
2. Model-Klassen nur nach Absprache aendern.
3. Jede Person arbeitet hauptsaechlich in ihrem Service/UI-Bereich.
4. Vor dem Start immer aktuellen Stand holen:

```bash
git pull origin main
```

5. Regelmaessig kleine Commits machen.

Bsp fuer gute Commit Messages:

```text
Add product search
Implement cart total price
Add user registration validation
Create admin product delete method
```

---

## 11. Was ist noch nicht fertig?

Diese Grundlage ist absichtlich noch kein fertiger Shop.

Noch zu implementieren sind:

```text
Benutzeroberflaeche im ui-Ordner
vollstaendige Login-Logik
Passwort-Hashing
Produkt-Suche und Filter
Warenkorb-Logik
Bestellhistorie
Admin-Funktionen
Tests
```

Die Grundlage sorgt nur dafuer, dass alle mit denselben Klassen und Methodennamen starten.

---

## 12. Passwort-Hinweis

In den User Stories steht, dass Passwoerter nicht im Klartext gespeichert werden duerfen.

Darum verwenden wir:

```java
passwordHash
```

Person 1 sollte spaeter aus dem echten Passwort einen Hash erzeugen und nur den Hash speichern.

Keine Klartext-Passwoerter in `.dat` Dateien speichern.

---

## Benutzeroberfläche (UI)

Die Benutzeroberfläche wird mit Java Swing umgesetzt.

Alle Fenster und GUI-Komponenten werden im Paket:

src/ui

abgelegt.

Beispiele:
- LoginFrame
- RegisterFrame
- ProductListFrame
- CartFrame
- AdminFrame

JOptionPane darf für:
- Fehlermeldungen
- Erfolgsmeldungen
- Bestätigungsdialoge

verwendet werden.
