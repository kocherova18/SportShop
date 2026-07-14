package ui;

import model.User;
import javax.swing.*;
import java.awt.*;

public class ProfileFrame extends JFrame {
    private User user;

    public ProfileFrame(User user){
        this.user = user;

        setTitle("Mein Konto");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Mein Konto");
        JLabel nameLabel = new JLabel("Name: " + user.getName());
        JLabel emailLabel = new JLabel("E-Mail: " + user.getEmail());
        JLabel roleLabel = new JLabel("Rolle: " + user.getRole());

        JButton closeButton = new JButton("Schließen");
        closeButton.addActionListener(e -> dispose());

        panel.add(titleLabel);
        panel.add(nameLabel);
        panel.add(emailLabel);
        panel.add(roleLabel);
        panel.add(closeButton);

        add(panel);
    }

}