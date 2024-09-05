/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dream_team.easy_travel.mainApp;

/**
 * @author sifat
 */

import dream_team.easy_travel.Easy_Travel;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    public LoginPanel(Easy_Travel App) {
        setLayout(new GridBagLayout());
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel label = new JLabel("Login Page");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.WHITE);
        add(label, gbc);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(20);
        add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        add(passwordField, gbc);

        JButton loginButton = getjButton(App, usernameField, passwordField, this);
        add(loginButton, gbc);
    }

    private static JButton getjButton(Easy_Travel App, JTextField usernameField, JPasswordField passwordField, LoginPanel loginPanel) {
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e ->
        {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (username.equals("admin") && password.equals("admin")) {
                App.showPanel("Home");
            }else if (username.equals("user") && password.equals("user")) {
                App.showPanel("Home");
            }
            else {
                JOptionPane.showMessageDialog(loginPanel, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        return loginButton;
    }
}