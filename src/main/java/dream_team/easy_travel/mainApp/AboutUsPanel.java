/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dream_team.easy_travel.mainApp;

/**
 *
 * @author sifat
 */
import dream_team.easy_travel.Easy_Travel;
import javax.swing.*;
import java.awt.*;

public class AboutUsPanel extends JPanel {
    public AboutUsPanel(Easy_Travel app) {
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        JLabel label = new JLabel("About Us Page", SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);

        JButton goToHomeButton = new JButton("Go to Home");
        goToHomeButton.addActionListener(e -> app.showPanel("Home"));

        add(goToHomeButton, BorderLayout.SOUTH);
    }
}