package dream_team.easy_travel.mainApp;

import dream_team.easy_travel.DatabaseConnection.ManageDatabase;
import dream_team.easy_travel.Easy_Travel;

import javax.swing.*;
import java.awt.*;

import static com.mysql.cj.conf.PropertyKey.password1;

public class LoginPanel extends JPanel {
    public LoginPanel(Easy_Travel app) {
        setLayout(new GridBagLayout());
        setBackground(Color.GRAY);

        JLabel EmailField = new JLabel("Email:");
        JTextField EmailInputField = new JTextField(20);
        JLabel PasswordLabel = new JLabel("Password:");
        JTextField passwordField = new JTextField(20);

        JButton loginButton = new JButton("Login");

        JLabel gotoSignUp = new JLabel("Don't have an account? Sign Up");
        gotoSignUp.setForeground(Color.BLUE);
        gotoSignUp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gotoSignUp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                app.showPanel("SignUp");
            }
        });

        loginButton.addActionListener(e -> {
            String username = EmailInputField.getText();
            String password = passwordField.getText();
            if(username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields");
                return;
            }
            else{
                try {
                    ManageDatabase db = new ManageDatabase();
                    User user = db.getUserByUsername(username);
                    if (user != null && user.getPassword().equals(password)) {
                        JOptionPane.showMessageDialog(null, "Login Successful");
                        app.setLoggedInUser(user);
                        app.showPanel("Home");
                        app.updateFrameTitle("Home");

                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid Username or Password");
                    }

                } catch (Exception ex) {
                    System.err.println(ex);
                }

            }

        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(EmailField, gbc);
        gbc.gridx = 1;
        add(EmailInputField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(PasswordLabel, gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(loginButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(gotoSignUp, gbc);

    }
}