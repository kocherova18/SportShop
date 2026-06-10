package service;

import model.Address;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private List<User> users;

    public UserService(List<User> users) {
        this.users = users != null ? users : new ArrayList<>();
    }

    public boolean register(User user) {
        // TODO Person 1: E-Mail pruefen, Passwort hashen, User speichern
        return false;
    }

    public User login(String email, String password) {
        // TODO Person 1: Passwort hashen/vergleichen und passenden User zurueckgeben
        return null;
    }

    public boolean changePassword(User user, String oldPassword, String newPassword) {
        // TODO Person 1: altes Passwort pruefen, neues Passwort hashen und speichern
        return false;
    }

    public boolean updateProfile(User user, String name, Address address) {
        // TODO Person 1: Profilfelder pruefen und speichern
        return false;
    }

    public List<User> getAllUsers() {
        return users;
    }

    public User getUserById(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }
}
