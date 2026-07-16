package service;

import data.DataManager;
import model.User;
import model.Address;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class UserService {
    private List<User> users;
    private DataManager dataManager;

    public UserService(DataManager dataManager){
        this.dataManager = dataManager;
        this.users = dataManager.loadUsers();
    }

    public User register(String email, String password){
        validateEmail(email);
        validatePassword(password);

        String cleanEmail = email.trim().toLowerCase();

        if(isAlreadyRegistered(cleanEmail)){
            throw new IllegalArgumentException("Diese E-mail-Adresse ist bereits registriert.");
        }

        int newID = generateUserId();
        String passwordHash = hashPassword(password);

        User newUser = new User(newID, "Kunde", cleanEmail, passwordHash, null, User.ROLE_CUSTOMER);

        users.add(newUser);
        dataManager.saveUsers(users);

        return newUser;
    }

    public User login(String email, String password){
        if(email==null || email.trim().isEmpty()){
            throw new IllegalArgumentException("E-mail darf nicht leer sein.");
        }

        if(password==null || password.isEmpty()){
            throw new IllegalArgumentException("Passwort darf nicht leer sein.");
        }

        User user = findUserByEmail(email);

        if(user == null){
            throw new IllegalArgumentException("E-mail oder passwort ist falsch.");
        }

        String passwordHash = hashPassword(password);

        if(!passwordHash.equals(user.getPasswordHash())){
            throw new IllegalArgumentException("E-mail oder Passwort ist falsch.");
        }

        return user;
    }

    public void changePassword(User user, String oldPassword, String newPassword){
        if (user == null){
            throw new IllegalArgumentException("Sie müssen eingeloggt sein.");
        }

        if(oldPassword == null || oldPassword.isEmpty()){
            throw new IllegalArgumentException("Altes passwort darf nicht leer sein.");
        }

        validatePassword(newPassword);

        User savedUser = null;

        for(User currentUser : users){
            if(currentUser.getId() == user.getId()){
                savedUser = currentUser;
                break;
            }
        }

        if(savedUser == null){
            throw new IllegalArgumentException("Benutzer wurde nicht gefunden");
        }

        String oldPasswordHash = hashPassword(oldPassword);

        if(!oldPasswordHash.equals(savedUser.getPasswordHash())){
            throw new IllegalArgumentException("Altes passwort ist falsch.");
        }

        String newPasswordHash = hashPassword(newPassword);

        savedUser.setPasswordHash(newPasswordHash);
        user.setPasswordHash(newPasswordHash);

        dataManager.saveUsers(users);
    }

    public void updateProfile(User user, String name, Address address){
        if (user == null) {
            throw new IllegalArgumentException("Sie müssen eingeloggt sein.");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name darf nicht leer sein.");
        }

        validateAddress(address);

        User savedUser = null;

        for (User currentUser : users) {
            if (currentUser.getId() == user.getId()) {
                savedUser = currentUser;
                break;
            }
        }

        if (savedUser == null) {
            throw new IllegalArgumentException("Benutzer wurde nicht gefunden.");
        }

        Address cleanAddress = new Address(address.getStreet().trim(), address.getHouseNumber().trim(),
                address.getZipCode().trim(), address.getCity().trim(), address.getCountry().trim());

        savedUser.setName(name.trim());
        savedUser.setAddress(address);

        user.setName(name.trim());
        user.setAddress(address);

        dataManager.saveUsers(users);
    }

    private boolean isAlreadyRegistered(String email){
        if (email == null){     //falls jemand gibt kein Email ein und aktiviert die Methode,
            return false;       //dann wird er keine Fehlermeldung bekommen
        }
        String checkedEmail = email.trim();     //trim löscht Leerzeichen von Email-Adresse
        for(User user:users){
            if(user.getEmail().equalsIgnoreCase(checkedEmail)){     //identifiziert große und kleine Buchstaben als derselbe
                return true;
            }
        }
        return false;
    }

    private User findUserByEmail(String email){
        if (email == null){
            return null;
        }

        String checkedEmail = email.trim().toLowerCase();

        for(User user : users){
            if(user.getEmail() != null && user.getEmail().equalsIgnoreCase(checkedEmail)){
                return user;
            }
        }
        return null;
    }

    private void validateEmail(String email){
        if(email == null || email.trim().isEmpty()){
            throw new IllegalArgumentException("Email darf nicht leer sein");
        }

        String checkedEmail = email.trim();

        if(!checkedEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")){   //^bedeutet Anfang
            throw new IllegalArgumentException("Email hat ein ungültiges Format");
        }
    }

    private void validatePassword(String password){
        if(password == null || password.length()<8){
            throw new IllegalArgumentException("Passwort muss mindestens 8 Zeichen enthalten");
        }
    }

    private void validateAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Adresse muss vollständig ausgefüllt werden.");
        }

        if (address.getStreet() == null || address.getStreet().trim().isEmpty()) {
            throw new IllegalArgumentException("Strasse darf nicht leer sein.");
        }

        if (address.getHouseNumber() == null || address.getHouseNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Hausnummer darf nicht leer sein.");
        }

        if (address.getZipCode() == null || address.getZipCode().trim().isEmpty()) {
            throw new IllegalArgumentException("PLZ darf nicht leer sein.");
        }

        if (address.getCity() == null || address.getCity().trim().isEmpty()) {
            throw new IllegalArgumentException("Stadt darf nicht leer sein.");
        }

        if (address.getCountry() == null || address.getCountry().trim().isEmpty()) {
            throw new IllegalArgumentException("Land darf nicht leer sein.");
        }
    }

    private int generateUserId(){
        int maxID = 0;

        for(User user: users){
            if(user.getId()>maxID) {
                maxID = user.getId();
            }
        }

        return maxID+1;
    }
    
    private String hashPassword(String password){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");    //SHA-256 macht aus dem Text Hash
            byte[] hashedBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));      //SHA-256 arbeitet auf
                                                                                                //Bytes, nicht Strings
            StringBuilder result = new StringBuilder();     //Bytes werden als normales Text gespeichert

            for(byte b: hashedBytes){
                result.append(String.format("%02x", b));    //jedes Byte wrid in Hexadezimalsystem als Text gespeichert
            }
            return result.toString();
        } catch(NoSuchAlgorithmException e){
            throw new RuntimeException("Passwort konnte nicht gehasht werden,", e);
        }

    }


}

