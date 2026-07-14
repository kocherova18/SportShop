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

        LoginFrame loginFrame = new LoginFrame(userService);
        loginFrame.setVisible(true);
    }
}
