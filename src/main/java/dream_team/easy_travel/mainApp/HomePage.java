package dream_team.easy_travel.mainApp;

import dream_team.easy_travel.Easy_Travel;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class HomePage extends JPanel {

    public HomePage(Easy_Travel app) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        ImageIcon imageIcon = loadImageIcon();
        if (imageIcon == null) {
            throw new RuntimeException("Failed to load image: /HomeBG.png");
        }
        JLabel imageLabel = new JLabel(imageIcon);
        add(imageLabel, BorderLayout.NORTH);

        Font lobsterFont = loadFont();
        if (lobsterFont == null) {
            lobsterFont = new Font("Arial", Font.BOLD, 60);
        }

        JLabel textLabel = new JLabel("Welcome", SwingConstants.LEFT);
        textLabel.setForeground(Color.BLACK);
        textLabel.setFont(lobsterFont.deriveFont(60f));
        textLabel.setBounds(100, 200, 600, 50); // Set bounds for the textLabel
        imageLabel.add(textLabel);

        // Split the text at \n to create multiple JLabels
        String[] lines = {
                "We help you to find wonderful trips and great vacation",
                "Place and we will provide famous and popular tourist",
                "Place all over the world"
        };

        int yOffset = 270;
        for (String line : lines) {
            JLabel textLabel2 = new JLabel(line, SwingConstants.LEFT);
            textLabel2.setForeground(Color.BLACK);
            textLabel2.setFont(new Font("SansSerif", Font.ITALIC, 20));
            textLabel2.setBounds(100, yOffset, 600, 30); // Set bounds for each textLabel
            imageLabel.add(textLabel2);
            yOffset += 30;
        }

    CustomButton exploreButton = new CustomButton("Explore", 30);
        exploreButton.setBounds(120, 385, 150, 50);
        imageLabel.add(exploreButton);

        exploreButton.addActionListener(e -> {
             app.showPanel("Blog");
        });

        JPanel centerPanel = new JPanel(new GridBagLayout());
        add(centerPanel, BorderLayout.CENTER);
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

    private Font loadFont() {
        try (InputStream is = getClass().getResourceAsStream("/Lobster-Regular.ttf")) {
            assert is != null;
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            return font;
        } catch (FontFormatException | IOException | NullPointerException e) {
            System.err.println("Failed to load font: " + "/Lobster-Regular.ttf");
            return null;
        }
    }
}