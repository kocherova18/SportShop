import data.DataManager;
import service.AdminService;
import service.UserService;
import ui.LoginFrame;

public class Main {

    public static void main(String[] args) {
        DataManager dataManager = new DataManager();

        AdminService adminService =
                new AdminService(dataManager);

        adminService.createDefaultAdminIfMissing();

        UserService userService =
                new UserService(dataManager);

        LoginFrame loginFrame = new LoginFrame(userService);
        loginFrame.setVisible(true);
    }
}
