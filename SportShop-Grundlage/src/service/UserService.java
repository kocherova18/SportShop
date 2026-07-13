package service;

import data.DataManager;
import model.User;

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

    private void validateEmail(String email){
        if(email == null || email.trim().isEmpty()){
            throw new IllegalArgumentException("Email darf nicht leer sein");
        }

        String checkedEmail = email.trim();

        if(!checkedEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]\\.[A-Za-z]{2,}$")){ //^bedeutet Anfang
            throw new IllegalArgumentException("Email hat ein ungültiges Format");
        }
    }

    private void validatePassword(String password){
        if(password == null || password.length()<8){
            throw new IllegalArgumentException("Passwort muss mindestens 8 Zeichen enthalten");
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
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder result = new StringBuilder();

            for(byte b: hashedBytes){
                result.append(String.format("%02x", b));
            }
            return result.toString();
        } catch(NoSuchAlgorithmException e){
            throw new RuntimeException("Passwort konnte nicht gehasht werden,", e);
        }

    }


}

