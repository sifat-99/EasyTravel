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
import java.util.Objects;

public class AboutUsPanel extends JPanel {
    public AboutUsPanel(Easy_Travel app) {
        setLayout(new BorderLayout());
        ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/HomeBG.png")));
        JLabel imageLabel = new JLabel(imageIcon);
        add(imageLabel, BorderLayout.NORTH);

        JButton goToHomeButton = new JButton("Go to Home");
        goToHomeButton.addActionListener(e -> app.showPanel("Home"));
        goToHomeButton.setBounds(0, 700, 1200, 50);

        imageLabel.add(goToHomeButton, BorderLayout.SOUTH);
    }
}