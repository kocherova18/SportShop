package service;

import data.DataManager;
import model.User;

import java.util.List;

public class AdminService {

    private DataManager dataManager;

    public AdminService(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void createDefaultAdminIfMissing() {

        List<User> users = dataManager.loadUsers();

        for (User user : users) {
            if (user.isAdmin()) {
                return;
            }
        }

        User admin = new User(
                getNextUserId(users),
                "Administrator",
                "admin@sportshop.de",
                UserService.hashPassword("admin123"),
                null,
                User.ROLE_ADMIN
        );

        users.add(admin);
        dataManager.saveUsers(users);
    }

    private int getNextUserId(List<User> users) {

        int highestId = 0;

        for (User user : users) {
            if (user.getId() > highestId) {
                highestId = user.getId();
            }
        }

        return highestId + 1;
    }
}