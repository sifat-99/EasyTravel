package dream_team.easy_travel.mainApp;

import dream_team.easy_travel.DatabaseConnection.ManageDatabase;
import dream_team.easy_travel.Easy_Travel;

import javax.swing.*;
import java.sql.SQLException;

public class SignUp extends JPanel {

    public SignUp(Easy_Travel app) {
        setLayout(null);
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setBounds(100, 100, 100, 30);
        add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(200, 100, 200, 30);
        add(nameField);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(100, 150, 100, 30);
        add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(200, 150, 200, 30);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(100, 200, 100, 30);
        add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(200, 200, 200, 30);
        add(passwordField);

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(200, 250, 200, 30);
        signUpButton.addActionListener(e -> {
            String name = nameField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields");
                return;
            }
            ManageDatabase db = new ManageDatabase();
            try {
                if (db.getUserByUsername(username) != null) {
                    JOptionPane.showMessageDialog(this, "Username already exists");
                    return;
                }
                db.addNewUser(name, username, password);
                JOptionPane.showMessageDialog(this, "User created successfully, Please login");
                app.showPanel("Login");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        add(signUpButton);

        JButton goToLoginButton = new JButton("Go to Login");
        goToLoginButton.setBounds(200, 300, 200, 30);
        goToLoginButton.addActionListener(e -> app.showPanel("Login"));
        add(goToLoginButton);
    }
}
