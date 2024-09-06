package dream_team.easy_travel.mainApp;

import dream_team.easy_travel.Easy_Travel;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Blog extends JPanel {
    public Blog(Easy_Travel app) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        ImageIcon imageIcon = loadImageIcon();
        if (imageIcon == null) {
            throw new RuntimeException("Failed to load image: /HomeBG.png");
        }
        JLabel imageLabel = new JLabel(imageIcon);
        add(imageLabel, BorderLayout.NORTH);

    }

    private ImageIcon loadImageIcon() {
        try {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/HomeBG.png")));
            Image image = icon.getImage().getScaledInstance(1200, 750, Image.SCALE_SMOOTH);
            return new ImageIcon(image);
        } catch (NullPointerException e) {
            System.err.println("Resource not found: " + "/HomeBG.png");
            return null;
        }
    }


}
