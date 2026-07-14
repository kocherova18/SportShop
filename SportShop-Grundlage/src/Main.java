import data.DataManager;
import model.User;
import service.UserService;
import ui.RegisterFrame;

import java.sql.SQLOutput;

public class Main {

    public static void main(String[] args) {
        DataManager dataManager = new DataManager();
        UserService userService = new UserService(dataManager);

        //RegisterFrame registerFrame = new RegisterFrame(userService);
        //registerFrame.setVisible(true);

        try{
            User user = userService.login("example@example.com", "password123");

            System.out.println("Login erfolgreich.");
            System.out.println("Eingeloggt als: " + user.getEmail());
        }catch(IllegalArgumentException e){
            System.out.println("Fehler: " + e.getMessage());
        }

       /* try{
            User user = userService.register("example@example.com", "password123");

            System.out.println("Registrierung erfolgreich.");
            System.out.println("Benutzer: " + user);
        } catch (IllegalArgumentException e){
            System.out.println("Fehler: " + e.getMessage());
        }
        */
    }
}
