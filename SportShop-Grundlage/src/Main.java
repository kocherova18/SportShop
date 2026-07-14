import data.DataManager;
import model.User;
import service.UserService;
import ui.RegisterFrame;
import ui.LoginFrame;
import java.sql.SQLOutput;

public class Main {

    public static void main(String[] args) {
        DataManager dataManager = new DataManager();
        UserService userService = new UserService(dataManager);

        String email = "example@example.com";
        String oldPassword = "password123";
        String newPassword = "newpassword123";

        try {
            User user = userService.login(email, oldPassword);

            userService.changePassword(user, oldPassword, newPassword);

            System.out.println("Passwort wurde erfolgreich geändert.");

            UserService checkService = new UserService(dataManager);
            User loggedInAgain = checkService.login(email, newPassword);

            System.out.println("Login mit neuem Passwort erfolgreich.");
            System.out.println("Eingeloggt als: " + loggedInAgain.getEmail());

        } catch (IllegalArgumentException e) {
            System.out.println("Fehler: " + e.getMessage());
        }
    }
}
